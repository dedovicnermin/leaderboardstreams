package io.nermdev.kafka.leaderboardstreams.framework;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.RecordTooLargeException;
import org.apache.kafka.streams.errors.ProductionExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class SCBProductionExceptionHandler implements ProductionExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(SCBProductionExceptionHandler.class);
    @Override
    public ProductionExceptionHandlerResponse handle(ProducerRecord<byte[], byte[]> producerRecord, Exception e) {
        e.printStackTrace();
        log.error("Exception message: {}", e.getMessage());
        Optional.ofNullable(producerRecord)
                        .ifPresent(
                                pr -> log.error(
                                        "Producer Record that could not be serialized (K,V): ({},{})",
                                        new String(producerRecord.key()),
                                        new String(producerRecord.value())
                                )
                        );
        if (e instanceof RecordTooLargeException) {
            // airport luggage solution can be done
            return ProductionExceptionHandlerResponse.CONTINUE;
        }
        return ProductionExceptionHandlerResponse.FAIL;
    }

    @Override
    public void configure(Map<String, ?> map) {}
}
