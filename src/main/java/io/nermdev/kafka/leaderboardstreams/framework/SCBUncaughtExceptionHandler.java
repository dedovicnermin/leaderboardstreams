package io.nermdev.kafka.leaderboardstreams.framework;

import org.apache.kafka.streams.errors.StreamsException;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * options:
 *  - Replace the thread
 *  - shut down task thread
 *  - shut down all task threads with same app-id
 */
public class SCBUncaughtExceptionHandler implements StreamsUncaughtExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(SCBUncaughtExceptionHandler.class);
    @Override
    public StreamThreadExceptionResponse handle(Throwable e) {
        e.printStackTrace();
        final Throwable cause = e.getCause();
        log.error("Uncaught Streams Exception message: {}", cause.getMessage());
        if (e instanceof StreamsException) {
            // wrapped user code exception
            if (cause.getMessage().equals("Retryable transient error")) {
                log.info("Replacing Stream Thread (post catching retryable transient error)...");
                return StreamThreadExceptionResponse.REPLACE_THREAD;
            }
            log.error("Cannot recover from Uncaught Streams Exception. Shutting down Kafka Streams client...");
            return StreamThreadExceptionResponse.SHUTDOWN_CLIENT;
        }
        log.error("Encountered uncaught exception, outside the scope of StreamsException. %nMSG: [{}] %nShutting down application...", e.getMessage());
        return StreamThreadExceptionResponse.SHUTDOWN_APPLICATION;
    }
}
