package org.throwable.server.configuration;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.throwable.server.event.MessageTransactionEventPublisher;
import org.throwable.server.event.listener.RegisterConfirmCallbackListener;
import org.throwable.server.event.listener.RegisterConfirmDeadLetterListener;
import org.throwable.server.event.listener.TransactionConfirmListener;
import org.throwable.server.event.listener.TransactionDelayConfirmListener;
import org.throwable.server.support.ServerListenerContainerManager;
import org.throwable.server.support.ServerRabbitComponentManager;
import org.throwable.server.support.kengen.DefaultKeyGenerator;
import org.throwable.server.support.kengen.KeyGenerator;
import org.throwable.support.validate.DefaultLoggingValidateFailStrategy;
import org.throwable.support.validate.ValidateFailStrategy;
import org.throwable.support.*;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 18:14
 */
@EnableConfigurationProperties(value = BaseServerProperties.class)
@Configuration
public class MessageTransactionServerAutoConfiguration {

    @Bean
    @DependsOn(value = "serverRabbitComponentManager")
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
    public ServerListenerContainerManager serverListenerContainerManager(ListenerContainerRegistrar registrar,
                                                                   ListenerContainerFactory factory) {
        return new ServerListenerContainerManager(registrar, factory);
    }

    @Bean
    public ServerRabbitComponentManager serverRabbitComponentManager(AmqpAdmin amqpAdmin) {
        return new ServerRabbitComponentManager(amqpAdmin);
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new DefaultKeyGenerator();
    }

    @ConditionalOnMissingBean
    @Bean
    public ValidateFailStrategy validateFailStrategy() {
        return new DefaultLoggingValidateFailStrategy();
    }

    @Bean
    public MessageTransactionEventPublisher messageTransactionEventPublisher() {
        return new MessageTransactionEventPublisher();
    }

    @Bean
    public RegisterConfirmCallbackListener registerConfirmCallbackListener() {
        return new RegisterConfirmCallbackListener();
    }

    @Bean
    public RegisterConfirmDeadLetterListener registerConfirmDeadLetterListener() {
        return new RegisterConfirmDeadLetterListener();
    }

    @Bean
    public TransactionConfirmListener transactionConfirmListener() {
        return new TransactionConfirmListener();
    }

    @Bean
    public TransactionDelayConfirmListener transactionDelayConfirmListener(){
        return new TransactionDelayConfirmListener();
    }
}
