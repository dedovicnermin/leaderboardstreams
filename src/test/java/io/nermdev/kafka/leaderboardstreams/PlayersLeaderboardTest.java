package io.nermdev.kafka.leaderboardstreams;

import io.nermdev.kafka.leaderboardstreams.models.json.LeaderboardInstance;
import io.nermdev.schemas.avro.leaderboards.Player;
import io.nermdev.schemas.avro.leaderboards.Product;
import io.nermdev.schemas.avro.leaderboards.ScoreCard;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.kstream.KStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

class PlayersLeaderboardTest extends BaseLeaderboardTest {

    static Topology topology;
    TopologyTestDriver testDriver;
    TestInputTopic<Long, ScoreCard> inputTopic;
    TestOutputTopic<Long, LeaderboardInstance> outputTopic;

    @BeforeAll
    static void buildTopology() {
        final StreamsBuilder builder = new StreamsBuilder();
        final KStream<Long, ScoreCard> source = LeaderboardTopology.createScoreCardEventSource(builder, scoreCardSerde);
        LeaderboardTopology.getAggregateHighestScoresForPlayer(source, scoreCardSerde).toStream().to(outputTestTopicName);
        topology = builder.build();
    }

    @BeforeEach
    void setup() {
        final Properties properties = new Properties();
        properties.putAll(serdeConfig);
        testDriver = new TopologyTestDriver(topology, properties);
        inputTopic = testDriver.createInputTopic(LeaderboardTopology.SOURCE_TOPIC_NAME, longSerde.serializer(), scoreCardSerde.serializer());
        outputTopic = testDriver.createOutputTopic(outputTestTopicName, longSerde.deserializer(), leaderboardSerde.deserializer());
    }

    @AfterEach
    void cleanup() {
        testDriver.close();
    }

    @ParameterizedTest
    @ValueSource(doubles = {1.24, 4.50, 10, 43, 100})
    void test(final double winningScore) {
        final Product product1 = new Product(990L, "COD");
        final Product product2 = new Product(991L, "NBA2K23");
        final Product product3 = new Product(991L, "GTAV");


        final Player player = new Player(1L, "Thomas Shelby");
        var sc1 = ScoreCard.newBuilder()
                .setPlayer(player)
                .setProduct(product1)
                .setScore(winningScore)
                .setLatestDate(LocalDate.now().toString())
                .build();
        var sc2 = new ScoreCard(player, product2, faker.number().randomDouble(3, 0, (long) winningScore), LocalDate.now().toString());
        var sc3 = new ScoreCard(player, product3, faker.number().randomDouble(3, 1, (long) winningScore), LocalDate.now().toString());
        inputTopic.pipeKeyValueList(
                Arrays.asList(
                        new KeyValue<>(player.getId(), sc1),
                        new KeyValue<>(player.getId(),sc2),
                        new KeyValue<>(player.getId(), sc3)
                )
        );
        Assertions.assertThat(outputTopic.getQueueSize()).isNotZero();

        final List<KeyValue<Long, LeaderboardInstance>> outputList = outputTopic.readKeyValuesToList();
        Assertions.assertThat(
                outputList.stream().map(kv-> kv.key)
                        .collect(Collectors.toSet())
        ).hasSize(1);

        final KeyValue<Long, LeaderboardInstance> finalLeaderboard = outputList.get(outputList.size() - 1);
        final List<ScoreCard> scoreCards = finalLeaderboard.value.toList();
        Assertions.assertThat(scoreCards).hasSize(3);
        assertFirstEntryInLeaderboardIsHighScore(scoreCards);
        Assertions.assertThat(finalLeaderboard.key).isEqualTo(player.getId());
        System.out.println(gson.toJson(finalLeaderboard));
    }

}
