package org.throwable.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.throwable.server.support.ListenerContainerFactory;
import org.throwable.server.support.ListenerContainerManager;
import org.throwable.server.support.ListenerContainerRegistrar;
import org.throwable.server.support.ListenerContainerRegistry;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 18:14
 */
@Configuration
public class MessageTransactionServerAutoConfiguration {

    @Bean
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
    public ListenerContainerManager listenerContainerManager(ListenerContainerRegistrar registrar,
                                                             ListenerContainerFactory factory) {
        return new ListenerContainerManager(registrar, factory);
    }
}
