package org.throwable.support;

import org.springframework.amqp.core.AcknowledgeMode;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 16:21
 */
public interface ConfigurableListener {

    /**
     * 配置目标队列
     *
     * @return String
     */
    String getTargetQueueName();

    /**
     * 配置ack模式
     *
     * @return AcknowledgeMode
     */
    AcknowledgeMode getAcknowledgeMode();

    /**
     * 配置初始化消费者数量
     *
     * @return Integer
     */
    Integer getConcurrentConsumerNumber();

    /**
     * 配置最大消费者数量
     *
     * @return Integer
     */
    Integer getMaxConcurrentConsumerNumber();
}
