package io.nermdev.kafka.leaderboardstreams;

import com.github.javafaker.Faker;
import io.nermdev.kafka.leaderboardstreams.models.json.Leaderboard;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

class PointsLeaderboardTopologyTest {
    Faker faker = Faker.instance();
    final Map<String, Object> serdeConfig = Map.of(
            StreamsConfig.APPLICATION_ID_CONFIG, "leaderboards",
            "schema.registry.url", "mock://leaderboards",
            "state.dir", "/tmp/kafka-streams-leaderboard-tests"+ UUID.randomUUID()
    );
    final Serde<ScoreCard> scorePlayerSerde = StreamUtils.getAvroSerde(serdeConfig);
    final Serde<Leaderboard> leaderboardSerde = StreamUtils.getJsonSerde(Leaderboard.class);

    TopologyTestDriver testDriver;
    TestInputTopic<Long, ScoreCard> scoreCardTestInputTopic;
    TestOutputTopic<?, ?> outputTopic;
    final String outputTestTopicName = "outbound.test";

    @BeforeEach
    void setup() {
        final Properties properties = new Properties();
        properties.putAll(serdeConfig);
        final Topology topology = new LeaderboardTopology().buildTopology(serdeConfig);
        System.out.println(topology.describe());
        testDriver = new TopologyTestDriver(topology.addSink("test.sink", "outbound.test", Serdes.Long().serializer(), leaderboardSerde.serializer(),"agg001"), properties);
        scoreCardTestInputTopic = testDriver.createInputTopic("leaderboard.scorecards", Serdes.Long().serializer(), scorePlayerSerde.serializer());
    }

    @AfterEach
    void shutdown() {
        testDriver.close();
    }

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
        outputTopic = testDriver.createOutputTopic(outputTestTopicName, Serdes.Long().deserializer(), leaderboardSerde.deserializer());
        scoreCardTestInputTopic.pipeKeyValueList(Arrays.asList(
                new KeyValue<>(sc1.getPlayer().getId(), sc1),
                new KeyValue<>( sc2.getPlayer().getId(),sc2),
                new KeyValue<>(sc3.getPlayer().getId(), sc3))
        );
        Assertions.assertThat(outputTopic.getQueueSize()).isNotZero();

    }
}
