package io.nermdev.kafka.leaderboardstreams.models.json;

import io.nermdev.schemas.avro.leaderboards.HistoricEntry;

import java.util.function.ToDoubleFunction;

public class LeaderboardHistoric extends Leaderboard<HistoricEntry> {

    public LeaderboardHistoric() {
        super();
    }

    @Override
    protected ToDoubleFunction<HistoricEntry> getDoubleFunction() {
        return HistoricEntry::getScore;
    }

    public LeaderboardHistoric add(final HistoricEntry record) {
        treeSet.stream()
                .filter(
                        he -> he.getPlayerId() == record.getPlayerId() &&
                                he.getProductId() == record.getProductId()
                )
                .findFirst()
                .ifPresent(treeSet::remove);
        treeSet.add(record);
        if (treeSet.size() > 10) treeSet.remove(treeSet.last());
        return this;
    }

}
