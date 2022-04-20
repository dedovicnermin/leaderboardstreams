package tech.nermindedovic.leaderboardstreams;

import org.apache.kafka.streams.KafkaStreams;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class LeaderboardApp {

    public static void main(String[] args) throws IOException {
        final Properties properties = StreamUtils.loadProperties();
        System.out.println(properties);
        final KafkaStreams streams = new KafkaStreams(null, properties);
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        streams.start();
    }
}
