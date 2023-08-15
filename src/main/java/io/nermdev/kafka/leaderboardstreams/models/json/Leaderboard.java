package io.nermdev.kafka.leaderboardstreams.models.json;

import io.nermdev.schemas.avro.leaderboards.ScoreCard;
import lombok.Data;


import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Data
public class Leaderboard {
    static final Comparator<ScoreCard> scoreCardComparator = Comparator.comparingDouble(ScoreCard::getScore);
    private final SortedSet<ScoreCard> treeSet;

    public Leaderboard() {
        this.treeSet = new TreeSet<>(scoreCardComparator);
    }

    public Leaderboard add(final ScoreCard record) {
        treeSet.add(record);
        if (treeSet.size() > 10) treeSet.remove(treeSet.last());
        return this;
    }

    public List<ScoreCard> toList() {
        return treeSet.stream().sorted(scoreCardComparator).collect(Collectors.toUnmodifiableList());
    }


}
