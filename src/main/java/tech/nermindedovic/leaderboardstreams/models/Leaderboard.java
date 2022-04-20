package tech.nermindedovic.leaderboardstreams.models;

import lombok.Data;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Data
public class Leaderboard {
    private final TreeSet<ScorePlayerRecord> treeSet = new TreeSet<>(Comparator.comparingDouble(ScorePlayerRecord::getScore));

    public Leaderboard add(final ScorePlayerRecord record) {
        treeSet.add(record);
        if (treeSet.size() > 10) treeSet.remove(treeSet.last());
        return this;
    }

    public Set<ScorePlayerRecord> getLeaderboardRecords() {
        return treeSet;
    }
}
