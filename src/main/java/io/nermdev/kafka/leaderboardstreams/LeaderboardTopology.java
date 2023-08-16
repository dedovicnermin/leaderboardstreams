package io.nermdev.kafka.leaderboardstreams;


import io.nermdev.kafka.leaderboardstreams.models.json.Leaderboard;
import io.nermdev.kafka.leaderboardstreams.models.json.LeaderboardHistoric;
import io.nermdev.kafka.leaderboardstreams.models.json.PPID;
import io.nermdev.schemas.avro.leaderboards.HistoricEntry;

import io.nermdev.schemas.avro.leaderboards.ScoreCard;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Initializer;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Map;

public class LeaderboardTopology {
    private static final Serde<Long> longSerdes = Serdes.Long();
    private static final Serde<Leaderboard> leaderboardSerdes = StreamUtils.getJsonSerde(Leaderboard.class);
    private static final Serde<LeaderboardHistoric> leaderboardHistoricSerde = StreamUtils.getJsonSerde(LeaderboardHistoric.class);
    private static final Serde<PPID> ppidSerde = StreamUtils.getJsonSerde(PPID.class);


    final StreamsBuilder builder = new StreamsBuilder();


    public Topology buildTopology(final Map<String,Object> serdeConfig) {
        final Serde<ScoreCard> scoreCardSerde = StreamUtils.getAvroSerde(serdeConfig);
        final Serde<HistoricEntry> historicEntrySerde = StreamUtils.getAvroSerde(serdeConfig);
        final KStream<Long, ScoreCard> scoreCardKStream = createScoreCardEventSource(scoreCardSerde);
        // stream processors

        final KStream<Long, ScoreCard> mScoreCardKStream = updateTimestamp(scoreCardKStream);
        getAggregateHighestScoresForPlayer(mScoreCardKStream, scoreCardSerde).toStream(Named.as("agg001"));
        getAggregateHighestScoresForProduct(mScoreCardKStream, scoreCardSerde).toStream(Named.as("agg002"));

        final KStream<PPID, ScoreCard> ppidScoreCardKStream = getPpidScoreCardKStream(mScoreCardKStream);
        final KTable<PPID, HistoricEntry> ppidHistoricEntryKStream = getHistoricEntryKStream(historicEntrySerde, ppidSerde, ppidScoreCardKStream);



        final KStream<Long, LeaderboardHistoric> historicPlayerKStream = getAggregateHistoricHighestScoresForPlayer(historicEntrySerde, ppidHistoricEntryKStream).toStream(Named.as("agg-player-historic"));

        final KStream<Long, LeaderboardHistoric> historicProductKStream = getAggregateHistoricHighestScoresForProduct(historicEntrySerde, ppidHistoricEntryKStream).toStream(Named.as("agg-product-historic"));
        historicProductKStream.print(Printed.toSysOut());
        return builder.build();
    }

    private static KTable<Long, LeaderboardHistoric> getAggregateHistoricHighestScoresForProduct(Serde<HistoricEntry> historicEntrySerde, KTable<PPID, HistoricEntry> ppidHistoricEntryKStream) {
        return ppidHistoricEntryKStream
                .toStream()
                .selectKey((ppid, historicEntry) -> ppid.getProductId(), Named.as("lbs-selectkey-ppid-to-productId"))
                .groupByKey(Grouped.with("lbs-groupbykey-post-ppid-to-productid",longSerdes, historicEntrySerde))
                .aggregate(
                        LeaderboardHistoric::new,
                        (playerId, record, leaderboard) -> leaderboard.add(record),

                        Materialized.<Long, LeaderboardHistoric, KeyValueStore<Bytes, byte[]>>as(LeaderboardService.LEADERBOARDS_STATE_STORE_TOP_10_HISTORIC_SCORES_PRODUCT)
                                .withKeySerde(longSerdes)
                                .withValueSerde(leaderboardHistoricSerde)
                );
    }

    private static KTable<Long, LeaderboardHistoric> getAggregateHistoricHighestScoresForPlayer(Serde<HistoricEntry> historicEntrySerde, KTable<PPID, HistoricEntry> ppidHistoricEntryKStream) {
        return ppidHistoricEntryKStream
                .toStream()
                .selectKey((ppid, historicEntry) -> ppid.getPlayerId(), Named.as("lbs-selectkey-ppid-to-playerId"))
                .groupByKey(Grouped.with("lbs-groupbykey-post-ppid-to-playerid",longSerdes, historicEntrySerde))
                .aggregate(
                        LeaderboardHistoric::new,
                        (playerId, record, leaderboard) -> leaderboard.add(record),
                        Materialized.<Long, LeaderboardHistoric, KeyValueStore<Bytes, byte[]>>as(LeaderboardService.LEADERBOARDS_STATE_STORE_TOP_10_HISTORIC_SCORES_GLOBAL)
                                .withKeySerde(longSerdes)
                                .withValueSerde(leaderboardHistoricSerde)
                );
    }


    private static KTable<PPID, HistoricEntry> getHistoricEntryKStream(Serde<HistoricEntry> historicEntrySerde, Serde<PPID> ppidSerde, KStream<PPID, ScoreCard> ppidScoreCardKStream) {
        return ppidScoreCardKStream
                .mapValues(scoreCard -> new HistoricEntry(scoreCard.getPlayer().getId(), scoreCard.getPlayer().getName(), scoreCard.getProduct().getId(), scoreCard.getProduct().getName(), scoreCard.getScore(), 1L))
                .groupByKey(Grouped.with("lbs-groupby-ppid", ppidSerde, historicEntrySerde))
                .reduce(
                        (oldEntry, newEntry) -> {
                            oldEntry.setScore(oldEntry.getScore() + newEntry.getScore());
                            oldEntry.setEntries(oldEntry.getEntries() + 1L);
                            return oldEntry;
                        },
                        Materialized.<PPID, HistoricEntry, KeyValueStore<Bytes, byte[]>>as(LeaderboardService.LEADERBOARDS_STATE_STORE_PPID_HISTORIC)
                                .withKeySerde(ppidSerde)
                                .withValueSerde(historicEntrySerde)
                );
    }

    private static KStream<PPID, ScoreCard> getPpidScoreCardKStream(KStream<Long, ScoreCard> mScoreCardKStream) {
        return mScoreCardKStream
                .selectKey((playerId, scoreCard) -> new PPID(playerId, scoreCard.getProduct().getId()), Named.as("lbs-selectKey-playerId-to-ppid"));
    }


    /**
     * aggregate top score instances for respective player
     * @return table of top 10 scores for respective player (leaderboard)
     */
    private KTable<Long, Leaderboard> getAggregateHighestScoresForPlayer(final KStream<Long, ScoreCard> scoreCardKStream, final Serde<ScoreCard> scoreCardSerde) {
        final Initializer<Leaderboard> initializer = Leaderboard::new;
        final Aggregator<Long, ScoreCard, Leaderboard> aggregator = (aLong, record, leaderboard) -> leaderboard.add(record);
        return scoreCardKStream.groupByKey(Grouped.with(longSerdes,scoreCardSerde))
                .aggregate(
                        initializer,
                        aggregator,
                        Materialized.<Long, Leaderboard, KeyValueStore<Bytes, byte[]>>as(LeaderboardService.LEADERBOARDS_STATE_STORE_TOP_10_SCORES_GLOBAL)
                                .withKeySerde(longSerdes)
                                .withValueSerde(leaderboardSerdes)
                );
    }



    private KTable<Long, Leaderboard> getAggregateHighestScoresForProduct(final KStream<Long, ScoreCard> scoreCardKStream, final Serde<ScoreCard> scoreCardSerde) {
        final Initializer<Leaderboard> initializer = Leaderboard::new;
        final Aggregator<Long, ScoreCard, Leaderboard> aggregator = (aLong, record, leaderboard) -> leaderboard.add(record);
        return scoreCardKStream
                .groupBy(
                        (k, v) -> v.getProduct().getId(), Grouped.with(longSerdes,scoreCardSerde)
                )
                .aggregate(
                        initializer,
                        aggregator,
                        Materialized.<Long, Leaderboard, KeyValueStore<Bytes, byte[]>>as(LeaderboardService.LEADERBOARDS_STATE_STORE_TOP_10_SCORES_PRODUCT)
                                .withKeySerde(longSerdes)
                                .withValueSerde(leaderboardSerdes)
                );
    }




    private KStream<Long, ScoreCard> updateTimestamp(final KStream<Long, ScoreCard> scoreCardKStream) {
        return scoreCardKStream.mapValues(
                scoreCard -> {
                    scoreCard.setLatestDate(DateFormatter.formatDateToString(System.currentTimeMillis(), "CST"));
                    return scoreCard;
                });
    }



    private KStream<Long, ScoreCard> createScoreCardEventSource(Serde<ScoreCard> scoreCardSerde) {
        return builder.stream("leaderboard.scorecards", Consumed.with(Serdes.Long(), scoreCardSerde));
    }

}
