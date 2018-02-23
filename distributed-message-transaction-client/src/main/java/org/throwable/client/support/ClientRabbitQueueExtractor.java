package org.throwable.client.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.throwable.common.RabbitConstants;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 17:51
 */
public class ClientRabbitQueueExtractor{

    @Autowired
    private ApplicationInfoExtractor applicationInfoExtractor;

    public String getTriggerQueue() {
        return getQueueBound(RabbitConstants.TRIGGER_QUEUE_NAME);
    }

    public String getCheckQueue() {
        return getQueueBound(RabbitConstants.CHECK_QUEUE_NAME);
    }

    private String getQueueBound(String queueName) {
        return String.format("%s.%s.%s", queueName, applicationInfoExtractor.extractApplicationName(), applicationInfoExtractor.extractInstanceSign());
    }
}
