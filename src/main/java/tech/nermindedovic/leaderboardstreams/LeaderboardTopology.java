package tech.nermindedovic.leaderboardstreams;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
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

    /**
     * Group by key and aggregate
     * @param enrichedRecordStream keyed by ProductID
     * @param scorePlayerSerde enriched player card with all information needed for display
     * @return table of top 5 players (leaderboard), keyed by product
     */
    private KTable<Long, Leaderboard> getAggregate(final KStream<Long, ScorePlayerRecord> enrichedRecordStream, final Serde<ScorePlayerRecord> scorePlayerSerde) {
        final Initializer<Leaderboard> initializer = Leaderboard::new;
        final Aggregator<Long, ScorePlayerRecord, Leaderboard> aggregator = (aLong, record, leaderboard) -> leaderboard.add(record);
        return enrichedRecordStream.groupByKey(Grouped.with(longSerdes,scorePlayerSerde))
                .aggregate(
                        initializer,
                        aggregator,
                        Materialized.<Long, Leaderboard, KeyValueStore<Bytes, byte[]>>as(LeaderboardService.LEADERBOARD_STORE_NAME)
                                .withKeySerde(longSerdes)
                                .withValueSerde(leaderboardSerdes)
                );
    }

    /**
     * Enrich record with productName
     * @param productEventTable containing product name, keyed by ID
     * @param rekeyedScorePlayerStream  stream of records with player/score information, lacking product name (null)
     * @return enriched playerscore record
     */
    private KStream<Long, ScorePlayerRecord> enrichWithProductName(final GlobalKTable<Long, Product> productEventTable, final KStream<Long, ScorePlayerRecord> rekeyedScorePlayerStream) {
        return rekeyedScorePlayerStream
                .leftJoin(productEventTable, (aLong, record) -> aLong, (record, product) -> {
                    record.setProductName(product.getName().toString());
                    return record;
                });
    }

    /**
     * Rekey enriched PlayerScoreRecord stream in order to satisfy co-partitioning requirement
     * @param scorePlayerStream stream of enriched score events with player information
     * @return rekeyed stream of ScorePlayerRecord
     */
    private KStream<Long, ScorePlayerRecord> getRekeyedScorePlayerStream(final KStream<Long, ScorePlayerRecord> scorePlayerStream) {
        return scorePlayerStream.selectKey(((aLong, scorePlayerRecord) -> scorePlayerRecord.getProductId()));
    }


    /**
     * ScoreEvent and Player event Stream-Table join
     * @param scoreEventSerde serde for ScoreEvent
     * @param playerEventSerde serde for PlayerEvent
     * @param scoreEventStream stream of ScoreEvent records
     * @param playerEventTable state store of Player event records
     * @return enriched record containing score/player information
     */
    private KStream<Long, ScorePlayerRecord> getScorePlayerStream(
            final Serde<ScoreEvent> scoreEventSerde, Serde<Player> playerEventSerde,
            final KStream<Long, ScoreEvent> scoreEventStream,
            final KTable<Long, Player> playerEventTable
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


    /**
     * Source processor for Product events
     * @param productEventSerde serde for Product record, keyed by Long
     * @return Global state store containing Product information, keyed by ID
     */
    private GlobalKTable<Long, Product> createProductEventSource(final Serde<Product> productEventSerde) {
        return builder.globalTable(PRODUCT_EVENTS_TOPIC, Consumed.with(longSerdes, productEventSerde));
    }

    /**
     * Source processor for Player events
     * @param playerEventSerde serde for Player record, keyed by Long
     * @return Sharded state store containing Player information, keyed by ID
     */
    private KTable<Long, Player> createPlayerEventSource(final Serde<Player> playerEventSerde) {
        return builder.table(PLAYER_EVENTS_TOPIC, Consumed.with(longSerdes, playerEventSerde));
    }

    /**
     * Source processor for ScoreEvent records. Re-keyed in preparation for joining with Player.
     * @param scoreEventSerde serde for ScoreEvent (NO KEY SENT ON INBOUND)
     * @return Stream of ScoreEvent records
     */
    private KStream<Long, ScoreEvent> createScoreEventSourceProcessor(final Serde<ScoreEvent> scoreEventSerde) {
        return builder.stream(SCORE_EVENTS_TOPIC, Consumed.with(Serdes.String(), scoreEventSerde))
                .selectKey(( key, scoreEvent ) -> scoreEvent.getPlayerId());
    }

}
