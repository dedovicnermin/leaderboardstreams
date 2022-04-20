package tech.nermindedovic.leaderboardstreams.models;


import lombok.Builder;
import lombok.Data;

import java.util.Comparator;

@Data
@Builder
public class ScorePlayerRecord implements Comparator<ScorePlayerRecord> {

    private Long productId;
    private String productName;
    private Long playerId;
    private String playerName;
    private String playerDOB;
    private Double score;

    public ScorePlayerRecord(Long productId, String productName, Long playerId, String playerName, String playerDOB, Double score) {
        this.productId = productId;
        this.playerId = playerId;
        this.playerName = playerName;
        this.playerDOB = playerDOB;
        this.score = score;
    }

    @Override
    public int compare(ScorePlayerRecord o1, ScorePlayerRecord o2) {
        return 0;
    }
}
