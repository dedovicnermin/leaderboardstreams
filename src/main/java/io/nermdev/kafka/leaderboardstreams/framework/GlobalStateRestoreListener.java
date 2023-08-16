package io.nermdev.kafka.leaderboardstreams.framework;

import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.streams.processor.StateRestoreListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalStateRestoreListener implements StateRestoreListener {
    private static final Logger log = LoggerFactory.getLogger(GlobalStateRestoreListener.class);
    @Override
    public void onRestoreStart(final TopicPartition topicPartition, final String storeName, final long startingOffset, final long endingOffset) {
        log.info(
                "Started restoration of stateStore with name [{}] on paritition [{}]. %nTotal records to be restored (endOffset - startingOffset) : ({} - {}) = {} records to be restored.",
                storeName,
                topicPartition.partition(),
                endingOffset,
                startingOffset,
                (endingOffset - startingOffset)

        );
    }

    @Override
    public void onBatchRestored(final TopicPartition topicPartition, final String storeName, final long batchEndOffset, final long numRestored) {
        log.info("Restored batch ({}) for storeName ({}) and partition ({})",
                numRestored,
                storeName,
                topicPartition.partition()
        );
    }

    @Override
    public void onRestoreEnd(final TopicPartition topicPartition, final String storeName, final long totalRestored) {
        log.info(
                "Restoration complete for storeName ({}) and partition ({})",
                storeName,
                topicPartition.partition()
        );
    }
}
