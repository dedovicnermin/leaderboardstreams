package tech.nermindedovic.leaderboardstreams;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.nermindedovic.leaderboardstreams.models.Leaderboard;
import tech.nermindedovic.leaderboardstreams.models.ScorePlayerRecord;
import tech.nermindedovic.leaderboardstreams.models.avro.Player;
import tech.nermindedovic.leaderboardstreams.models.avro.Product;
import tech.nermindedovic.leaderboardstreams.models.avro.ScoreEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Only test that should pass is the last one.
 * Why? - TDD
 * How to run other tests:
 *   insert sink processor to OUTPUT_TOPIC with respective Produced.with(keyserdes, valueserdes)
 */
class LeaderboardTopologyTest {
    final LeaderboardTopology leaderboardTopology = new LeaderboardTopology();
    final Map<String, Object> serdeConfig = Map.of(
            StreamsConfig.APPLICATION_ID_CONFIG, "leaderboards",
            "schema.registry.url", "mock://leaderboards"
    );
    final Serde<ScoreEvent> scoreEventSerde = StreamUtils.getAvroSerde(serdeConfig);
    final Serde<Player> playerEventSerde = StreamUtils.getAvroSerde(serdeConfig);
    final Serde<Product> productEventSerde = StreamUtils.getAvroSerde(serdeConfig);
    final Serde<ScorePlayerRecord> scorePlayerSerde = StreamUtils.getJsonSerde(ScorePlayerRecord.class);

    TopologyTestDriver testDriver;
    TestInputTopic<String, ScoreEvent> scoreEventInput;
    TestInputTopic<Long, Player> playerEventInput;
    TestInputTopic<Long, Product> productEventInput;
    TestOutputTopic<?, ?> outputTopic;

    @BeforeEach
    void setup() {
        final Properties properties = new Properties();
        properties.putAll(serdeConfig);
        testDriver = new TopologyTestDriver(leaderboardTopology.buildTopology(serdeConfig), properties);
        scoreEventInput = testDriver.createInputTopic(LeaderboardTopology.SCORE_EVENTS_TOPIC, Serdes.String().serializer(), scoreEventSerde.serializer());
        playerEventInput = testDriver.createInputTopic(LeaderboardTopology.PLAYER_EVENTS_TOPIC, Serdes.Long().serializer(), playerEventSerde.serializer());
        productEventInput = testDriver.createInputTopic(LeaderboardTopology.PRODUCT_EVENTS_TOPIC, Serdes.Long().serializer(), productEventSerde.serializer());
    }

    @Test
    void testScoreEventWillConsumeAndRekey() {
        outputTopic = testDriver.createOutputTopic(LeaderboardTopology.OUTBOUND_TOPIC, Serdes.Long().deserializer(), scoreEventSerde.deserializer());
        final ScoreEvent scoreEvent = new ScoreEvent(0L, 0L, 0.0);
        scoreEventInput.pipeInput(null,scoreEvent);
        assertThat(outputTopic.readKeyValue()).isEqualTo(KeyValue.pair(0L,scoreEvent));
    }

    @Test
    void testPlayerEventsGetConsumedToTable() {
        outputTopic = testDriver.createOutputTopic(LeaderboardTopology.OUTBOUND_TOPIC, Serdes.Long().deserializer(), playerEventSerde.deserializer());
        final Player playerEvent = new Player(1L, "Test Dummy", "01/01/2011");
        playerEventInput.pipeInput(1L, playerEvent);
        assertThat(outputTopic.readKeyValue()).isEqualTo(KeyValue.pair(1L, playerEvent));
    }

    @Test
    void testScoreEventPlayerJoin() {
        outputTopic = testDriver.createOutputTopic(LeaderboardTopology.OUTBOUND_TOPIC, Serdes.Long().deserializer(), scorePlayerSerde.deserializer());
        final Player player1 = new Player(1L, "player1", "01/01/01");
        final ScoreEvent scoreEvent = new ScoreEvent(player1.getId(), 1L, 0.0);

        playerEventInput.pipeInput(player1.getId(), player1);
        scoreEventInput.pipeInput(null, scoreEvent);

        final ScorePlayerRecord expected = new ScorePlayerRecord(scoreEvent.getProductId(), null, player1.getId(), player1.getName().toString(), player1.getDOB().toString(), scoreEvent.getScore());

        assertThat(outputTopic.readKeyValue()).isEqualTo(KeyValue.pair(1L,expected));

    }


    @Test
    void testAfterJoinWithPlayer_streamIsRekeyedToProductId() {
        outputTopic = testDriver.createOutputTopic(LeaderboardTopology.OUTBOUND_TOPIC, Serdes.Long().deserializer(), scorePlayerSerde.deserializer());
        final Player player100 = new Player(100L, "player100", "09/09/09");
        final ScoreEvent scoreEvent = new ScoreEvent(100L, 5L, 19.92);

        playerEventInput.pipeInput(player100.getId(), player100);
        scoreEventInput.pipeInput(null, scoreEvent);

        final KeyValue<Long, ScorePlayerRecord> expected = KeyValue.pair(
                scoreEvent.getProductId(),
                new ScorePlayerRecord(scoreEvent.getProductId(),null, player100.getId(), player100.getName().toString(), player100.getDOB().toString(), scoreEvent.getScore())
        );

        assertThat(outputTopic.readKeyValue()).isEqualTo(expected);
    }

    @Test
    void testAfterRekey_streamIsEnrichedWithProductName() {
        outputTopic = testDriver.createOutputTopic(LeaderboardTopology.OUTBOUND_TOPIC, Serdes.Long().deserializer(), scorePlayerSerde.deserializer());
        final Product product = new Product(1000L, "MW2");
        final Player player = new Player(100L, "Player100", "10/01/11");
        final ScoreEvent scoreEvent = new ScoreEvent(player.getId(), product.getId(), 123.23);

        productEventInput.pipeInput(product.getId(), product);
        playerEventInput.pipeInput(player.getId(), player);
        scoreEventInput.pipeInput(null, scoreEvent);

        final KeyValue<Long, ScorePlayerRecord> expected = KeyValue.pair(
                product.getId(),
                ScorePlayerRecord.builder().productId(product.getId())
                        .productName(product.getName().toString())
                        .playerId(player.getId())
                        .playerName(player.getName().toString())
                        .playerDOB(player.getDOB().toString())
                        .score(scoreEvent.getScore())
                        .build()
        );

        assertThat(outputTopic.readKeyValue()).isEqualTo(expected);
    }

    @Test
    void testAfterEnriched_aggregationReturnsAccurateLeaderboards() {
        outputTopic = testDriver.createOutputTopic(LeaderboardTopology.OUTBOUND_TOPIC, Serdes.Long().deserializer(), StreamUtils.getJsonSerde(Leaderboard.class).deserializer());
        final Product product = new Product(1L, "Hardest Game Ever");
        final List<KeyValue<Long, Player>> players = getThreePlayers();
        final List<KeyValue<String, ScoreEvent>> scoreEvents = getThreeScoreEvents();


        productEventInput.pipeInput(product.getId(), product);
        playerEventInput.pipeKeyValueList(players);
        scoreEventInput.pipeKeyValueList(scoreEvents);

        final Leaderboard expectedLeaderboard = getAggregatedExpectedLeaderboard();

        assertThat(outputTopic.readKeyValuesToMap().get(1L)).isEqualTo(expectedLeaderboard);

    }





    private Leaderboard getAggregatedExpectedLeaderboard() {
        final Leaderboard leaderboard = new Leaderboard();
        leaderboard.add(new ScorePlayerRecord(1L, "HardestGameEver", 100L, "player1", "01/01/01", 100.00));
        leaderboard.add(new ScorePlayerRecord(1L, "HardestGameEver", 200L, "player2", "02/02/02", 75.00));
        leaderboard.add(new ScorePlayerRecord(1L, "HardestGameEver", 200L, "player3", "03/03/03", 50.00));
        return leaderboard;
    }

    private List<KeyValue<String, ScoreEvent>> getThreeScoreEvents() {
        return Arrays.asList(
                KeyValue.pair(null, new ScoreEvent(100L, 1L, 100.00)),
                KeyValue.pair(null, new ScoreEvent(200L, 1L, 75.00)),
                KeyValue.pair(null, new ScoreEvent(300L, 1L, 50.00))
        );
    }


    private List<KeyValue<Long, Player>> getThreePlayers() {
        return Arrays.asList(
                KeyValue.pair(100L, new Player(100L, "player1", "01/01/01")),
                KeyValue.pair(200L, new Player(200L, "player2", "02/02/02")),
                KeyValue.pair(300L, new Player(300L, "player3", "03/03/03"))
        );
    }
}