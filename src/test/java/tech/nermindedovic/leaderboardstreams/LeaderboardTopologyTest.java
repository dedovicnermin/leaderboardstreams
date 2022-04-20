package tech.nermindedovic.leaderboardstreams;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.nermindedovic.leaderboardstreams.models.ScorePlayerRecord;
import tech.nermindedovic.leaderboardstreams.models.avro.Player;
import tech.nermindedovic.leaderboardstreams.models.avro.Product;
import tech.nermindedovic.leaderboardstreams.models.avro.ScoreEvent;

import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

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

//    @Test
//    void testAfterRekey_streamIs




}