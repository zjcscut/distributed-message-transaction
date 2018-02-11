package org.throwable.server.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 16:02
 */
@Data
@ConfigurationProperties(prefix = "dmt.server.base")
public class BaseServerProperties {

    private static final Long DEFAULT_WORKER_ID = 1L;
    private static final Long DEFAULT_CONFIRM_INTERVAL_MILLISECOND = 10000L;
    private static final int DEFAULT_CONCURRENT_CONSUMER_NUMBER = 5;
    private static final int DEFAULT_MAX_CONCURRENT_CONSUMER_NUMBER = 10;

    /**
     * 主键生成器依赖的进程id，分布式环境下每个实例的进程id必须不相同
     */
    private Long keygenWorkerId = DEFAULT_WORKER_ID;
    /**
     * 确认间隔，默认为5000毫秒
     */
    private Long confirmIntervalMillisecond = DEFAULT_CONFIRM_INTERVAL_MILLISECOND;

    /**
     * 注册事务消费者队列初始消费者数量
     */
    private Integer registerConcurrentConsumerNumber = DEFAULT_CONCURRENT_CONSUMER_NUMBER;

    /**
     * 注册事务消费者队列最大消费者数量
     */
    private Integer registerMaxConcurrentConsumerNumber = DEFAULT_MAX_CONCURRENT_CONSUMER_NUMBER;

    /**
     * 确认事务消费者队列初始消费者数量
     */
    private Integer confirmConcurrentConsumerNumber = DEFAULT_CONCURRENT_CONSUMER_NUMBER;

    /**
     * 确认事务消费者队列最大消费者数量
     */
    private Integer confirmMaxConcurrentConsumerNumber = DEFAULT_MAX_CONCURRENT_CONSUMER_NUMBER;

    /**
     * 延迟确认事务消费者队列初始消费者数量
     */
    private Integer delayConfirmConcurrentConsumerNumber = DEFAULT_CONCURRENT_CONSUMER_NUMBER;

    /**
     * 延迟确认事务消费者队列最大消费者数量
     */
    private Integer delayConfirmMaxConcurrentConsumerNumber = DEFAULT_MAX_CONCURRENT_CONSUMER_NUMBER;
}
