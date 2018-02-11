package org.throwable.client.configuration;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.throwable.client.support.*;
import org.throwable.support.ListenerContainerFactory;
import org.throwable.support.ListenerContainerRegistrar;
import org.throwable.support.ListenerContainerRegistry;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 14:30
 */
@EnableConfigurationProperties(value = BaseClientProperties.class)
@Configuration
public class MessageTransactionClientAutoConfiguration {

    @Autowired
    private BaseClientProperties baseClientProperties;

    @Bean
    @DependsOn(value = "clientRabbitComponentManager")
    public ListenerContainerFactory listenerContainerFactory() {
        return new ListenerContainerFactory();
    }

    @Bean
    public ListenerContainerRegistry listenerContainerRegistry() {
        return new ListenerContainerRegistry();
    }

    @Bean
    public ListenerContainerRegistrar listenerContainerRegistrar(ListenerContainerRegistry listenerContainerRegistry) {
        return new ListenerContainerRegistrar(listenerContainerRegistry);
    }

    @Bean
    public ApplicationInfoExtractor applicationInfoExtractor() {
        return new ApplicationInfoExtractor();
    }

    @Bean
    public ClientRabbitComponentManager clientRabbitComponentManager(AmqpAdmin amqpAdmin,
                                                                     RabbitTemplate rabbitTemplate,
                                                                     ClientRabbitQueueExtractor clientRabbitQueueExtractor) {
        return new ClientRabbitComponentManager(amqpAdmin, clientRabbitQueueExtractor, rabbitTemplate);
    }

    @Bean
    @DependsOn(value = "clientRabbitComponentManager")
    public ClientListenerContainerManager clientListenerContainerManager(ListenerContainerRegistrar listenerContainerRegistrar,
                                                                         ListenerContainerFactory listenerContainerFactory) {
        return new ClientListenerContainerManager(listenerContainerRegistrar, listenerContainerFactory);
    }

    @Bean
    public ClientRabbitQueueExtractor clientRabbitQueueExtractor() {
        return new ClientRabbitQueueExtractor();
    }

    @Bean
    public MessageTransactionThreadPoolProvider messageTransactionThreadPoolProvider() {
        return new MessageTransactionThreadPoolProvider(
                baseClientProperties.getCorePoolSize(),
                baseClientProperties.getMaxPoolSize(),
                baseClientProperties.getPoolQueueCapacity(),
                baseClientProperties.getKeepAliveSecond()
        );
    }

    @Bean
    public MessageTransactionTemplate messageTransactionTemplate() {
        return new MessageTransactionTemplate();
    }
}
