package org.throwable.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/5 9:58
 */
@ToString
@Data
public class ListenerContainerMetadata {

    private final Class<?> listenerClass;
    private final String containerBeanName;
    private final Integer concurrentConsumers;
    private final Integer maxConcurrentConsumers;
    private final String[] targetQueues;

    public ListenerContainerMetadata(Builder builder) {
        this.listenerClass = builder.listenerClass;
        this.containerBeanName = builder.containerBeanName;
        this.concurrentConsumers = builder.concurrentConsumers;
        this.maxConcurrentConsumers = builder.maxConcurrentConsumers;
        this.targetQueues = builder.targetQueues;
    }

    public static class Builder {

        private Class<?> listenerClass;
        private String containerBeanName;
        private Integer concurrentConsumers;
        private Integer maxConcurrentConsumers;
        private String[] targetQueues;

        public Builder setListenerClass(Class<?> listenerClass) {
            this.listenerClass = listenerClass;
            return this;
        }

        public Builder setContainerBeanName(String containerBeanName) {
            this.containerBeanName = containerBeanName;
            return this;
        }

        public Builder setConcurrentConsumers(Integer concurrentConsumers) {
            this.concurrentConsumers = concurrentConsumers;
            return this;
        }

        public Builder setMaxConcurrentConsumers(Integer maxConcurrentConsumers) {
            this.maxConcurrentConsumers = maxConcurrentConsumers;
            return this;
        }

        public Builder setTargetQueues(String[] queueNames){
            this.targetQueues = queueNames;
            return this;
        }

        public ListenerContainerMetadata build() {
            return new ListenerContainerMetadata(this);
        }
    }
}
