package io.nermdev.kafka.leaderboardstreams;

import io.nermdev.kafka.leaderboardstreams.models.json.ScorePlayerRecord;
import io.nermdev.schemas.avro.leaderboards.Player;
import io.nermdev.schemas.avro.leaderboards.Product;
import io.nermdev.schemas.avro.leaderboards.ScoreEvent;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

public class DepracatedLeaderboardTopology {
    public static final String SCORE_EVENTS_TOPIC = "leaderboard.scores";
    public static final String PLAYER_EVENTS_TOPIC = "leaderboard.players";
    public static final String PRODUCT_EVENTS_TOPIC = "leaderboard.products";
    private static final Serde<Long> longSerdes = Serdes.Long();
    final StreamsBuilder builder = new StreamsBuilder();
    /**
     * Enrich record with productName
     * @param productEventTable containing product name, keyed by ID
     * @param rekeyedScorePlayerStream  stream of records with player/score information, lacking product name (null)
     * @return enriched playerscore record
     */
    private KStream<Long, ScorePlayerRecord> enrichWithProductName(final GlobalKTable<Long, Product> productEventTable, final KStream<Long, ScorePlayerRecord> rekeyedScorePlayerStream) {
        return rekeyedScorePlayerStream
                .leftJoin(productEventTable, (aLong, record) -> aLong, (record, product) -> {
                    record.setProductName(product.getName());
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
                                .playerName(playerEvent.getName())
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
