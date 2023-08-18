package io.nermdev.kafka.leaderboardstreams;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.nermdev.kafka.leaderboardstreams.models.json.Leaderboard;
import io.nermdev.kafka.leaderboardstreams.models.json.LeaderboardInstance;
import io.nermdev.kafka.leaderboardstreams.utils.StreamUtils;
import io.nermdev.schemas.avro.leaderboards.ScoreCard;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.assertj.core.api.Assertions;

import java.util.List;
import java.util.Map;
import java.util.UUID;

class BaseLeaderboardTest {

    protected static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    protected static final Faker faker = Faker.instance();
    protected static final Map<String, Object> serdeConfig = Map.of(
            StreamsConfig.APPLICATION_ID_CONFIG, "leaderboards",
            "schema.registry.url", "mock://leaderboards",
            "state.dir", "/tmp/kafka-streams-leaderboard-tests"+ UUID.randomUUID()
    );
    static final Serde<Long> longSerde = Serdes.Long();
    static final Serde<ScoreCard> scoreCardSerde = StreamUtils.getAvroSerde(serdeConfig);
    static final Serde<LeaderboardInstance> leaderboardSerde = StreamUtils.getJsonSerde(LeaderboardInstance.class);

    static final String outputTestTopicName = "outbound.test";

    protected void assertFirstEntryInLeaderboardIsHighScore(final List<ScoreCard> actualScorecards) {
        double maxScore = 0;
        for (var scard : actualScorecards) {
            if (scard.getScore() > maxScore) {
                maxScore = scard.getScore();
            }
        }
        final ScoreCard finalScore = actualScorecards.get(actualScorecards.size() - 1);
        Assertions.assertThat(finalScore.getScore()).isEqualTo(maxScore);
    }
}
