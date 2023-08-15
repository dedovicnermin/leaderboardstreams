package io.nermdev.kafka.leaderboardstreams.models.json;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScorePlayerRecord implements Comparable<ScorePlayerRecord> {

    private Long productId;
    private String productName;
    private Long playerId;
    private String playerName;
    private String playerDOB;
    private Double score;

    public ScorePlayerRecord(Long productId, String productName, Long playerId, String playerName, String playerDOB, Double score) {
        this.productId = productId;
        this.productName = productName;
        this.playerId = playerId;
        this.playerName = playerName;
        this.playerDOB = playerDOB;
        this.score = score;
    }

    @Override
    public int compareTo(ScorePlayerRecord o) {
        return Double.compare(o.score, this.getScore());
    }
}
