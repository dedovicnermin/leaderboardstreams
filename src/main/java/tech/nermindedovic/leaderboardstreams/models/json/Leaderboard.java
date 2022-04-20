package tech.nermindedovic.leaderboardstreams.models.json;

import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Data
public class Leaderboard {
    static final Comparator<ScorePlayerRecord> scorePlayerRecordComparator = Comparator.comparingDouble(ScorePlayerRecord::getScore);
    private final TreeSet<ScorePlayerRecord> treeSet;

    public Leaderboard() {
        this.treeSet = new TreeSet<>(scorePlayerRecordComparator);
    }

    public Leaderboard add(final ScorePlayerRecord record) {
        treeSet.add(record);
        if (treeSet.size() > 10) treeSet.remove(treeSet.last());
        return this;
    }

    public List<ScorePlayerRecord> toList() {
        return treeSet.stream().collect(Collectors.toUnmodifiableList());
    }
}
