package io.nermdev.kafka.leaderboardstreams.framework;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.errors.DeserializationExceptionHandler;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;


public class SCBDeserializationExceptionHandler implements DeserializationExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(SCBDeserializationExceptionHandler.class);

    @Override
    public DeserializationHandlerResponse handle(ProcessorContext processorContext, ConsumerRecord<byte[], byte[]> consumerRecord, Exception e) {
        e.printStackTrace();
        log.error("Exception message : {}",e.getMessage());
        Optional.ofNullable(consumerRecord)
                        .ifPresent(
                                cr -> log.error(
                                        "Consumer Record that could not be deserialized (K,V): ({},{})",
                                        new String(consumerRecord.key()),
                                        new String(consumerRecord.value())
                                )
                        );
        return DeserializationHandlerResponse.FAIL;
    }

    @Override
    public void configure(Map<String, ?> map) {}
}
