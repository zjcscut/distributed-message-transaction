package org.throwable.client.support;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.throwable.support.NamedThreadFactory;

import java.util.concurrent.*;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 16:44
 */
public class MessageTransactionThreadPoolProvider implements InitializingBean, DisposableBean {

    private final Integer coreSize;
    private final Integer maxSize;
    private final Integer queueCapacity;
    private final Long keepAliveSeconds;
    private static final String DEFAULT_THREAD_PREFIX = "message-transaction-client";
    private static final RejectedExecutionHandler DEFAULT_REJECTED_EXECUTION_HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();
    private static final ThreadFactory DEFAULT_THREAD_FACTORY = new NamedThreadFactory(DEFAULT_THREAD_PREFIX,true);
    private ThreadPoolExecutor executor;

    public MessageTransactionThreadPoolProvider(Integer coreSize, Integer maxSize, Integer queueCapacity, Long keepAliveSeconds) {
        this.coreSize = coreSize;
        this.maxSize = maxSize;
        this.queueCapacity = queueCapacity;
        this.keepAliveSeconds = keepAliveSeconds;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.executor = new ThreadPoolExecutor(coreSize, maxSize, keepAliveSeconds, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueCapacity), DEFAULT_THREAD_FACTORY, DEFAULT_REJECTED_EXECUTION_HANDLER);
    }

    @Override
    public void destroy() throws Exception {
        if (null != executor && !executor.isShutdown()) {
            executor.shutdown();
        }
    }

    public ExecutorService getTransactionExecutor() {
        return this.executor;
    }
}
