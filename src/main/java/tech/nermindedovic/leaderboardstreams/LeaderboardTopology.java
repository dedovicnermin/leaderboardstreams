package tech.nermindedovic.leaderboardstreams;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import tech.nermindedovic.leaderboardstreams.models.json.Leaderboard;
import tech.nermindedovic.leaderboardstreams.models.json.ScorePlayerRecord;
import tech.nermindedovic.leaderboardstreams.models.avro.Player;
import tech.nermindedovic.leaderboardstreams.models.avro.Product;
import tech.nermindedovic.leaderboardstreams.models.avro.ScoreEvent;

import java.util.Map;

public class LeaderboardTopology {
    public static final String SCORE_EVENTS_TOPIC = "score-events";
    public static final String PLAYER_EVENTS_TOPIC = "player-events";
    public static final String PRODUCT_EVENTS_TOPIC = "product-events";
    public static final String OUTBOUND_TOPIC = "outbound-events";

    private static final Serde<Long> longSerdes = Serdes.Long();
    private static final Serde<Leaderboard> leaderboardSerdes = StreamUtils.getJsonSerde(Leaderboard.class);

    final StreamsBuilder builder = new StreamsBuilder();

    public Topology buildTopology(final Map<String,Object> serdeConfig) {
        final Serde<ScoreEvent> scoreEventSerde = StreamUtils.getAvroSerde(serdeConfig);
        final Serde<Player> playerEventSerde = StreamUtils.getAvroSerde(serdeConfig);
        final Serde<Product> productEventSerde = StreamUtils.getAvroSerde(serdeConfig);
        final Serde<ScorePlayerRecord> scorePlayerSerde = StreamUtils.getJsonSerde(ScorePlayerRecord.class);

        // source processors
        final KStream<Long, ScoreEvent> scoreEventStream = createScoreEventSourceProcessor(scoreEventSerde);
        final KTable<Long, Player> playerEventTable = createPlayerEventSource(playerEventSerde);
        final GlobalKTable<Long, Product> productEventTable = createProductEventSource(productEventSerde);

        // stream processors
        final KStream<Long, ScorePlayerRecord> scorePlayerStream = getScorePlayerStream(scoreEventSerde, playerEventSerde, scoreEventStream, playerEventTable);
        final KStream<Long, ScorePlayerRecord> rekeyedScorePlayerStream = getRekeyedScorePlayerStream(scorePlayerStream);
        final KStream<Long, ScorePlayerRecord> enrichedRecordStream = enrichWithProductName(productEventTable, rekeyedScorePlayerStream);
        final KStream<Long, Leaderboard> stream = getAggregate(enrichedRecordStream, scorePlayerSerde).toStream();
        stream.print(Printed.toSysOut());

        //sink
        stream.to(OUTBOUND_TOPIC, Produced.with(longSerdes, leaderboardSerdes));

        return builder.build();
    }

    private KTable<Long, Leaderboard> getAggregate(KStream<Long, ScorePlayerRecord> enrichedRecordStream, Serde<ScorePlayerRecord> scorePlayerSerde) {
        return enrichedRecordStream.groupByKey(Grouped.with(longSerdes,scorePlayerSerde))
                .aggregate(
                        Leaderboard::new,
                        (aLong, record, leaderboard) -> leaderboard.add(record),
                        Named.as(LeaderboardService.LEADERBOARD_STORE_NAME),
                        Materialized.with(longSerdes, leaderboardSerdes)
                );
    }

    private KStream<Long, ScorePlayerRecord> enrichWithProductName(GlobalKTable<Long, Product> productEventTable, KStream<Long, ScorePlayerRecord> rekeyedScorePlayerStream) {
        return rekeyedScorePlayerStream
                .leftJoin(productEventTable, (aLong, record) -> aLong, (record, product) -> {
                    record.setProductName(product.getName().toString());
                    return record;
                });
    }

    private KStream<Long, ScorePlayerRecord> getRekeyedScorePlayerStream(KStream<Long, ScorePlayerRecord> scorePlayerStream) {
        return scorePlayerStream.selectKey(((aLong, scorePlayerRecord) -> scorePlayerRecord.getProductId()));
    }

    private KStream<Long, ScorePlayerRecord> getScorePlayerStream(
            Serde<ScoreEvent> scoreEventSerde, Serde<Player> playerEventSerde,
            KStream<Long, ScoreEvent> scoreEventStream, KTable<Long, Player> playerEventTable
    ) {
        return scoreEventStream
                .leftJoin(
                        playerEventTable,
                        (scoreEvent, playerEvent) -> ScorePlayerRecord.builder()
                                .productId(scoreEvent.getProductId())
                                .playerId(scoreEvent.getPlayerId())
                                .playerName(playerEvent.getName().toString())
                                .playerDOB(playerEvent.getDOB().toString())
                                .score(scoreEvent.getScore())
                                .build()
                        , Joined.with(longSerdes, scoreEventSerde, playerEventSerde)
                );
    }

    private GlobalKTable<Long, Product> createProductEventSource(Serde<Product> productEventSerde) {
        return builder.globalTable(PRODUCT_EVENTS_TOPIC, Consumed.with(longSerdes, productEventSerde));
    }

    private KTable<Long, Player> createPlayerEventSource(Serde<Player> playerEventSerde) {
        return builder.table(PLAYER_EVENTS_TOPIC, Consumed.with(longSerdes, playerEventSerde));
    }

    private KStream<Long, ScoreEvent> createScoreEventSourceProcessor(Serde<ScoreEvent> scoreEventSerde) {
        return builder.stream(SCORE_EVENTS_TOPIC, Consumed.with(Serdes.String(), scoreEventSerde))
                .selectKey(( key, scoreEvent ) -> scoreEvent.getPlayerId());
    }

}
