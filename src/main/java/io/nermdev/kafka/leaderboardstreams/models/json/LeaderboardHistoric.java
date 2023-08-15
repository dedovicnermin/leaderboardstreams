package io.nermdev.kafka.leaderboardstreams.models.json;

import io.nermdev.schemas.avro.leaderboards.HistoricEntry;
import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Data
public class LeaderboardHistoric {

    static final Comparator<HistoricEntry> comparator = Comparator.comparingDouble(HistoricEntry::getScore);
    private final TreeSet<HistoricEntry> treeSet;

    public LeaderboardHistoric() {
        this.treeSet = new TreeSet<>(comparator);
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

    public List<HistoricEntry> toList() {
        return treeSet.stream().sorted(comparator).collect(Collectors.toUnmodifiableList());
    }
}
