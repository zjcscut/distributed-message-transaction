package org.throwable.server.support;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 18:16
 */
public class ListenerContainerRegistry implements BeanFactoryAware {

    private static final Map<String, Class<?>> CONTAINER_MAP = new HashMap<>();
    private DefaultListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    public void storeContainer(String beanName, Class<?> listenerClass) {
        CONTAINER_MAP.putIfAbsent(beanName, listenerClass);
    }

    public Map<String, Class<?>> getContainerMap() {
        return Collections.unmodifiableMap(CONTAINER_MAP);
    }

    public SimpleMessageListenerContainer getContainerBean(String beanName) {
        return beanFactory.getBean(beanName, SimpleMessageListenerContainer.class);
    }

    public SimpleMessageListenerContainer getContainerBean(Class<?> listenerClass) {
        String lookupKey = null;
        for (Map.Entry<String, Class<?>> entry : CONTAINER_MAP.entrySet()) {
            if (null != entry.getValue() && entry.getValue().equals(listenerClass)) {
                lookupKey = entry.getKey();
                break;
            }
        }
        return null != lookupKey ? beanFactory.getBean(lookupKey, SimpleMessageListenerContainer.class) : null;
    }
}
