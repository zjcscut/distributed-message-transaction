package org.throwable.support;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.throwable.model.dto.ListenerContainerMetadata;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 18:17
 */
public class ListenerContainerRegistrar implements BeanFactoryAware {

    private static final AtomicInteger COUNTER = new AtomicInteger(0);
    private DefaultListableBeanFactory beanFactory;
    private final ListenerContainerRegistry registry;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    public ListenerContainerRegistrar(ListenerContainerRegistry registry) {
        this.registry = registry;
    }

    public void registerListenerContainer(SimpleMessageListenerContainer container) {
        if (container instanceof ListenerContainerFactory.ExtendSimpleMessageListenerContainer) {
            ListenerContainerFactory.ExtendSimpleMessageListenerContainer extendContainer =
                    (ListenerContainerFactory.ExtendSimpleMessageListenerContainer) container;
            ListenerContainerMetadata metadata = extendContainer.getMetadata();
            beanFactory.registerSingleton(metadata.getContainerBeanName(), extendContainer);
            registry.storeContainer(metadata.getContainerBeanName(), metadata);
        } else {
            String beanName = String.format("%s-%d", SimpleMessageListenerContainer.class.getName(), COUNTER.incrementAndGet());
            beanFactory.registerSingleton(beanName, container);
            ListenerContainerMetadata metadata = new ListenerContainerMetadata.Builder()
                    .setListenerClass(container.getMessageListener().getClass())
                    .setContainerBeanName(beanName)
                    .setTargetQueues(container.getQueueNames())
                    .build();
            registry.storeContainer(beanName, metadata);
        }
    }
}
