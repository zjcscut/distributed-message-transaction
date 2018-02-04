package org.throwable.server.support;

import org.springframework.amqp.core.AcknowledgeMode;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 16:21
 */
public interface ConfigurableListener {

    String getTargetQueueName();

    AcknowledgeMode getAcknowledgeMode();

    Integer getConcurrentConsumerNumber();

    Integer getMaxConcurrentConsumerNumber();
}
