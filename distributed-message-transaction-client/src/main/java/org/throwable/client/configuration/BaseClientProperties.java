package org.throwable.client.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 14:28
 */
@Data
@ConfigurationProperties(prefix = "dmt.client.base")
public class BaseClientProperties {

	private static final int DEFAULT_TIMEOUT_SECOND = 5;
	private static final int DEFAULT_POOL_SIZE = 10;
	private static final int DEFAULT_POOL_QUEUE_SIZE = 100;

	private static final int DEFAULT_CONCURRENT_CONSUMER_NUMBER = 5;
	private static final int DEFAULT_MAX_CONCURRENT_CONSUMER_NUMBER = 10;

	private String instanceSign;

	private Integer triggerTransactionTimeoutSecond = DEFAULT_TIMEOUT_SECOND;

	private Integer executeTransactionTimeoutSecond = DEFAULT_TIMEOUT_SECOND;

	private Integer corePoolSize = DEFAULT_POOL_SIZE;

	private Integer maxPoolSize = DEFAULT_POOL_SIZE;

	private Long keepAliveSecond = 0L;

	private Integer poolQueueCapacity = DEFAULT_POOL_QUEUE_SIZE;

	private Integer triggerConcurrentConsumerNumber = DEFAULT_CONCURRENT_CONSUMER_NUMBER;
	private Integer triggerMaxConcurrentConsumerNumber = DEFAULT_MAX_CONCURRENT_CONSUMER_NUMBER;

	private Integer checkConcurrentConsumerNumber = DEFAULT_CONCURRENT_CONSUMER_NUMBER;
	private Integer checkMaxConcurrentConsumerNumber = DEFAULT_MAX_CONCURRENT_CONSUMER_NUMBER;

}
