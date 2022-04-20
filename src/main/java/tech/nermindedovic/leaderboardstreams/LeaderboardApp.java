package tech.nermindedovic.leaderboardstreams;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.state.HostInfo;

import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

import static io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;

public class LeaderboardApp {

    public static void main(String[] args) throws IOException {
        final Properties properties = StreamUtils.loadProperties();
        System.out.println(properties);
        final Topology topology = new LeaderboardTopology().buildTopology(
                Collections.singletonMap(
                        SCHEMA_REGISTRY_URL_CONFIG,
                        properties.getProperty(SCHEMA_REGISTRY_URL_CONFIG)
                )
        );
        System.out.println(topology.describe());
        final KafkaStreams streams = new KafkaStreams(
                topology,
                properties
        );
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        streams.start();

        final HostInfo hostInfo = new HostInfo("localhost", Integer.parseInt(properties.getProperty("application.port")));
        final LeaderboardService leaderboardService = new LeaderboardService(hostInfo, streams);
        leaderboardService.start();


    }
}
