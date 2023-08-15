package io.nermdev.kafka.leaderboardstreams.topology;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class PointsLeaderboardTopology {
    public static final String INPUT = "leaderboard.scorecards";

    private static final Serde<Long> longSerde = Serdes.Long();


}
