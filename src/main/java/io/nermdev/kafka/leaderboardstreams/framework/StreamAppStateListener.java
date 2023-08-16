package io.nermdev.kafka.leaderboardstreams.framework;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KafkaStreams.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Consumer;

public class StreamAppStateListener implements KafkaStreams.StateListener {
    private static final Logger log = LoggerFactory.getLogger(StreamAppStateListener.class);
    public static final String STATE_CHANGE = "STATE CHANGE: {}";
    Map<State, Consumer<State>> decisionTable = Map.of(
            State.CREATED, state -> log.info(STATE_CHANGE, State.CREATED),
            State.RUNNING,state -> log.info(STATE_CHANGE, State.RUNNING),
            State.REBALANCING, state -> log.info(STATE_CHANGE, State.REBALANCING),
            State.PENDING_ERROR, state -> log.error(STATE_CHANGE, State.PENDING_ERROR),
            State.ERROR, state -> log.error(STATE_CHANGE, State.ERROR),
            State.PENDING_SHUTDOWN, state -> log.error(STATE_CHANGE, State.PENDING_SHUTDOWN),
            State.NOT_RUNNING, state -> log.error(STATE_CHANGE, State.NOT_RUNNING)
    );

    @Override
    public void onChange(State oldState, State newState) {
        log.info("=== onChange(old={}, new={}) invoked ===", oldState, newState);
        decisionTable.get(newState).accept(newState);
    }
}
