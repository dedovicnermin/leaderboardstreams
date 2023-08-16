package io.nermdev.kafka.leaderboardstreams.models.json;

import lombok.Data;

@Data
public class PPID {
    private final Long playerId;
    private final Long productId;
}
