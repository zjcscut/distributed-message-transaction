package org.throwable.server.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * @author throwable
 * @version v1.0
 * @description 使用同步发布，因为在mq下异步再异步会有线程池任务积压的问题
 * @since 2018/2/5 17:07
 */
public class MessageTransactionEventPublisher implements ApplicationEventPublisherAware{

    private ApplicationEventPublisher delegate;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.delegate = applicationEventPublisher;
    }

    public void publish(ApplicationEvent applicationEvent){
        this.delegate.publishEvent(applicationEvent);
    }
}
