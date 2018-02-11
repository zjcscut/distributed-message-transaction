package org.throwable.support;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.Assert;
import org.throwable.exception.DuplicatedListenerContainerException;
import org.throwable.exception.ListenerBeanNotFoundException;
import org.throwable.model.dto.ListenerContainerMetadata;
import org.throwable.utils.ExtendClassUtils;

import java.util.Map;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 16:25
 */
public class ListenerContainerFactory implements BeanFactoryAware {

    private static final String CONTAINER_BEAN_NAME_SUFFIX = "-ListenerContainer";
    private static final String CONNECTION_FACTORY_QUALIFIER = "rabbitConnectionFactory";

    private DefaultListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    public SimpleMessageListenerContainer createListenerContainer(String listenerClassName) throws Exception {
        return createListenerContainer(Class.forName(listenerClassName));
    }

    public SimpleMessageListenerContainer createListenerContainer(Class<?> listenerClass) throws Exception {
        Assert.notNull(listenerClass, "Target listenerClass must not be null!");
        Assert.isTrue(ConfigurableListener.class.isAssignableFrom(listenerClass), String.format("Target listenerClass [%s] must implement interface <ConfigurableListener>!", listenerClass.getName()));
        ConnectionFactory connectionFactory = determineConnectionFactory();
        Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");
        RabbitAdmin rabbitAdmin = determineRabbitAdmin();
        Assert.notNull(rabbitAdmin, "RabbitAdmin must not be null!");
        Map<String, ?> listeners = beanFactory.getBeansOfType(listenerClass);
        Object listenerBeanToUse = null;
        if (null == listeners || listeners.isEmpty()) {
            listenerBeanToUse = getAndRegisterListener(listenerClass);
        } else if (listeners.size() > 1) {
            throw new DuplicatedListenerContainerException(String.format("Duplicated listener container for class [%s] in the application context", listenerClass.getName()));
        } else {
            for (Map.Entry<String, ?> entry : listeners.entrySet()) {
                listenerBeanToUse = entry.getValue();
            }
        }
        if (null == listenerBeanToUse) {
            throw new ListenerBeanNotFoundException(String.format("Listener bean not found for class [%s] in the application context", listenerClass.getName()));
        }
        ConfigurableListener configurableListener = (ConfigurableListener) listenerBeanToUse;
        String containerBeanName = determineContainerBeanName(listenerClass);
        ListenerContainerMetadata metadata = new ListenerContainerMetadata.Builder()
                .setContainerBeanName(containerBeanName)
                .setListenerClass(listenerClass)
                .setConcurrentConsumers(configurableListener.getConcurrentConsumerNumber())
                .setMaxConcurrentConsumers(configurableListener.getMaxConcurrentConsumerNumber())
                .setTargetQueues(new String[]{configurableListener.getTargetQueueName()})
                .build();
        ExtendSimpleMessageListenerContainer container = new ExtendSimpleMessageListenerContainer(metadata);
        container.setBeanName(containerBeanName);
        container.setMessageListener(listenerBeanToUse);
        container.setRabbitAdmin(rabbitAdmin);
        container.setConnectionFactory(determineConnectionFactory());
        container.setQueueNames(configurableListener.getTargetQueueName());
        container.setConcurrentConsumers(configurableListener.getConcurrentConsumerNumber());
        container.setMaxConcurrentConsumers(configurableListener.getMaxConcurrentConsumerNumber());
        container.setAcknowledgeMode(configurableListener.getAcknowledgeMode());
        return container;
    }

    private Object getAndRegisterListener(Class<?> listenerClass) {
        BeanDefinitionRegisterAssistor.INSTANCE.registerBeanDefinitionFromCommonClass(listenerClass, beanFactory);
        return beanFactory.getBean(ExtendClassUtils.INSTANCE.getShortClassName(listenerClass));
    }

    private String determineContainerBeanName(Class<?> listenerClass) {
        return String.format("%s%s", ExtendClassUtils.INSTANCE.getShortClassName(listenerClass), CONTAINER_BEAN_NAME_SUFFIX);
    }

    private ConnectionFactory determineConnectionFactory() {
        ConnectionFactory connectionFactory = null;
        try {
            connectionFactory = beanFactory.getBean(ConnectionFactory.class);
        } catch (Exception e) {
            //ignore
        }
        if (null == connectionFactory) {
            try {
                connectionFactory = (ConnectionFactory) beanFactory.getBean(CONNECTION_FACTORY_QUALIFIER);
            } catch (Exception e) {
                //ignore
            }
        }
        return connectionFactory;
    }

    private RabbitAdmin determineRabbitAdmin() {
        RabbitAdmin rabbitAdmin = null;
        try {
            rabbitAdmin = beanFactory.getBean(RabbitAdmin.class);
        } catch (Exception e) {
            //ignore
        }
        if (null == rabbitAdmin) {
            try {
                AmqpAdmin amqpAdmin = beanFactory.getBean(AmqpAdmin.class);
                rabbitAdmin = (RabbitAdmin) amqpAdmin;
            } catch (Exception e) {
                //ignore
            }
        }
        return rabbitAdmin;
    }

    public class ExtendSimpleMessageListenerContainer extends SimpleMessageListenerContainer {

        private final ListenerContainerMetadata metadata;

        public ExtendSimpleMessageListenerContainer(ListenerContainerMetadata metadata) {
            this.metadata = metadata;
        }

        public ListenerContainerMetadata getMetadata() {
            return metadata;
        }
    }
}
