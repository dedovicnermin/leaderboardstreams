package io.nermdev.kafka.leaderboardstreams.models.json;

import io.nermdev.schemas.avro.leaderboards.ScoreCard;

import java.util.function.ToDoubleFunction;


public class LeaderboardInstance extends Leaderboard<ScoreCard> {
    public LeaderboardInstance() {
        super();
    }

    @Override
    protected ToDoubleFunction<ScoreCard> getDoubleFunction() {
        return ScoreCard::getScore;
    }


    public LeaderboardInstance add(ScoreCard record) {
        treeSet.add(record);
        if (treeSet.size() > 10) treeSet.remove(treeSet.last());
        return this;
    }
}
