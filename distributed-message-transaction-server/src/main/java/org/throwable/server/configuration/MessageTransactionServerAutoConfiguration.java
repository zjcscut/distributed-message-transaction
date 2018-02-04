package org.throwable.server.configuration;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.throwable.server.support.*;

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
	@DependsOn(value = "rabbitComponentManager")
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

	@Bean
	public RabbitComponentManager rabbitComponentManager(AmqpAdmin amqpAdmin){
		return new RabbitComponentManager(amqpAdmin);
	}
}
