package io.nermdev.kafka.leaderboardstreams.models.json;


import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;


@Data
public abstract class Leaderboard<T> {
    protected final SortedSet<T> treeSet;
    protected Leaderboard() {
        this.treeSet = new TreeSet<>(Comparator.comparingDouble(getDoubleFunction()));
    }

    protected abstract ToDoubleFunction<T> getDoubleFunction();
    public abstract Leaderboard<T> add(final T record);

    public List<T> toList() {
        return treeSet.stream().sorted(Comparator.comparingDouble(getDoubleFunction())).collect(Collectors.toUnmodifiableList());
    }
}
