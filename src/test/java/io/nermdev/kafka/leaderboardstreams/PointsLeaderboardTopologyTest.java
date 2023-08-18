package io.nermdev.kafka.leaderboardstreams;

import com.github.javafaker.Faker;
import io.nermdev.kafka.leaderboardstreams.models.json.LeaderboardInstance;
import io.nermdev.kafka.leaderboardstreams.utils.StreamUtils;
import io.nermdev.schemas.avro.leaderboards.Player;
import io.nermdev.schemas.avro.leaderboards.Product;
import io.nermdev.schemas.avro.leaderboards.ScoreCard;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

class PointsLeaderboardTopologyTest {
    Faker faker = Faker.instance();
    static final Map<String, Object> serdeConfig = Map.of(
            StreamsConfig.APPLICATION_ID_CONFIG, "leaderboards",
            "schema.registry.url", "mock://leaderboards",
            "state.dir", "/tmp/kafka-streams-leaderboard-tests"+ UUID.randomUUID()
    );
    static final Serde<Long> longSerde = Serdes.Long();
    static final Serde<ScoreCard> scorePlayerSerde = StreamUtils.getAvroSerde(serdeConfig);
    static final Serde<LeaderboardInstance> leaderboardSerde = StreamUtils.getJsonSerde(LeaderboardInstance.class);

    TopologyTestDriver testDriver;
    static Topology topology;
    TestInputTopic<Long, ScoreCard> scoreCardTestInputTopic;
    TestOutputTopic<?, LeaderboardInstance> outputTopic;
    static final String outputTestTopicName = "outbound.test";


    @ParameterizedTest
    @ValueSource(doubles = {1.24, 4.50, 10, 43, 100})
    void testHighestScoreIsFirstInList(double winningScore) {
        final Product product = new Product(999L, "COD");
        var sc1 = ScoreCard.newBuilder()
                .setPlayer(new Player(1L, "WINNER"))
                .setProduct(product)
                .setScore(winningScore)
                .setLatestDate(LocalDate.now().toString())
                .build();
        var sc2 = new ScoreCard(new Player(2L, "LOSER"), product, faker.number().randomDouble(3, 0, (long) winningScore), LocalDate.now().toString());
        var sc3 = new ScoreCard(new Player(3L, "LOSER"), product, faker.number().randomDouble(3, 1, (long) winningScore), LocalDate.now().toString());
        outputTopic = testDriver.createOutputTopic(outputTestTopicName, longSerde.deserializer(), leaderboardSerde.deserializer());
        scoreCardTestInputTopic.pipeKeyValueList(Arrays.asList(
                new KeyValue<>(sc1.getPlayer().getId(), sc1),
                new KeyValue<>( sc2.getPlayer().getId(),sc2),
                new KeyValue<>(sc3.getPlayer().getId(), sc3))
        );
        Assertions.assertThat(outputTopic.getQueueSize()).isNotZero();


        final List<LeaderboardInstance> actualOutputList = outputTopic.readValuesToList();
        final LeaderboardInstance actualLeaderboard = actualOutputList.get(actualOutputList.size() - 1);
        final List<ScoreCard> actualScorecards = actualLeaderboard.toList();
        double maxScore = 0;
        for (var scard : actualScorecards) {
            if (scard.getScore() > maxScore) {
                maxScore = scard.getScore();
            }
        }
        Assertions.assertThat(actualScorecards.get(0).getScore()).isEqualTo(maxScore);
    }

    @BeforeEach
    void setup() {
        final Properties properties = new Properties();
        properties.putAll(serdeConfig);
        topology = new LeaderboardTopology().buildTopology(serdeConfig);
        testDriver = new TopologyTestDriver(topology.addSink("test.sink", "outbound.test", longSerde.serializer(), leaderboardSerde.serializer(),"agg001"), properties);
        scoreCardTestInputTopic = testDriver.createInputTopic("leaderboard.scorecards", longSerde.serializer(), scorePlayerSerde.serializer());
    }

    @AfterEach
    void shutdown() {
        testDriver.close();
    }

    @AfterAll
    static void afterAll() {
        System.out.println(topology.describe());
    }
}
