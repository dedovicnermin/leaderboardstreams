package io.nermdev.kafka.leaderboardstreams;
import io.nermdev.kafka.leaderboardstreams.framework.GlobalStateRestoreListener;
import io.nermdev.kafka.leaderboardstreams.framework.SCBUncaughtExceptionHandler;
import io.nermdev.kafka.leaderboardstreams.framework.StreamAppStateListener;
import io.nermdev.kafka.leaderboardstreams.utils.StreamUtils;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.state.HostInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


public class LeaderboardApp {
    static final int APP_PORT = 7000;
    static final Logger log = LoggerFactory.getLogger(LeaderboardApp.class);
    public static void main(String[] args) {
        if (args.length < 1) throw new IllegalArgumentException("Pass path to application.properties");
        final Properties properties = new Properties();
        StreamUtils.loadConfig(args[0], properties);
        final String appIp = System.getenv().getOrDefault("POD_IP", "localhost");
        final String appEndpoint = appIp+":"+APP_PORT;
        final String clientId = System.getenv().getOrDefault("POD_NAME", "local.client");
        properties.put(StreamsConfig.APPLICATION_SERVER_CONFIG, appEndpoint);
        properties.put(StreamsConfig.CLIENT_ID_CONFIG, clientId);
        log.info("==Properties=={}",properties);


        final KafkaStreams streams = createAndConfigureStreams(properties);
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        streams.start();

        final HostInfo hostInfo = new HostInfo(appIp, APP_PORT);
        final LeaderboardService leaderboardService = new LeaderboardService(hostInfo, streams);
        leaderboardService.start();
        Runtime.getRuntime().addShutdownHook(new Thread(leaderboardService::close));
    }

    private static KafkaStreams createAndConfigureStreams(final Properties properties) {
        final Topology topology = new LeaderboardTopology().buildTopology(StreamUtils.propertiesToMap(properties));
        log.info("== Topology == %n{}", topology.describe());
        final KafkaStreams streams = new KafkaStreams(
                topology,
                properties
        );
        streams.setStateListener(new StreamAppStateListener());
        streams.setGlobalStateRestoreListener(new GlobalStateRestoreListener());
        streams.setUncaughtExceptionHandler(new SCBUncaughtExceptionHandler());
        return streams;
    }
}
