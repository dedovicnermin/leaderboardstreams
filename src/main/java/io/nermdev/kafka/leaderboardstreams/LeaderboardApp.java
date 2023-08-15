package io.nermdev.kafka.leaderboardstreams;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.state.HostInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;




public class LeaderboardApp {
    static final Logger log = LoggerFactory.getLogger(LeaderboardApp.class);
    public static void main(String[] args) throws IOException {
        if (args.length < 1) throw new IllegalArgumentException("Pass path to application.properties");
        final Properties properties = new Properties();
        StreamUtils.loadConfig(args[0], properties);
        log.info("==Properties=={}",properties);


        final Topology topology = new LeaderboardTopology().buildTopology(StreamUtils.propertiesToMap(properties));
        log.info("== Topology == %n{}", topology.describe());
        final KafkaStreams streams = new KafkaStreams(
                topology,
                properties
        );
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        streams.start();
//InetAddress.getLocalHost().getHostName()
        final HostInfo hostInfo = new HostInfo("localhost", Integer.parseInt(properties.getProperty("application.port")));
        final LeaderboardService leaderboardService = new LeaderboardService(hostInfo, streams);
        leaderboardService.start();
        Runtime.getRuntime().addShutdownHook(new Thread(leaderboardService::close));
    }
}
