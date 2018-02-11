package org.throwable.support;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.throwable.model.dto.ListenerContainerMetadata;

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

    private static final Map<String, ListenerContainerMetadata> CONTAINER_MAP = new HashMap<>();
    private DefaultListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    public void storeContainer(String beanName, ListenerContainerMetadata metadata) {
        CONTAINER_MAP.putIfAbsent(beanName, metadata);
    }

    public Map<String, ListenerContainerMetadata> getContainerMap() {
        return Collections.unmodifiableMap(CONTAINER_MAP);
    }

    public SimpleMessageListenerContainer getContainerBean(String beanName) {
        return beanFactory.getBean(beanName, SimpleMessageListenerContainer.class);
    }

    public SimpleMessageListenerContainer getContainerBean(Class<?> listenerClass) {
        String lookupKey = null;
        for (Map.Entry<String, ListenerContainerMetadata> entry : CONTAINER_MAP.entrySet()) {
            if (null != entry.getValue() && entry.getValue().getListenerClass().equals(listenerClass)) {
                lookupKey = entry.getKey();
                break;
            }
        }
        return null != lookupKey ? beanFactory.getBean(lookupKey, SimpleMessageListenerContainer.class) : null;
    }

    public ListenerContainerMetadata getContainerMetadata(String beanName){
        return CONTAINER_MAP.get(beanName);
    }
}
