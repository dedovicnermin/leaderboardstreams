package tech.nermindedovic.leaderboardstreams.models;

import lombok.Data;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

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

    public Set<ScorePlayerRecord> getLeaderboardRecords() {
        return treeSet;
    }
}
