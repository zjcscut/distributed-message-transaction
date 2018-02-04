package org.throwable.server.support;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.InitializingBean;
import org.throwable.server.common.ServerConstants;

import java.util.HashSet;
import java.util.Set;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/5 0:09
 */
public class RabbitComponentManager implements InitializingBean {

	private static final Set<String> INTERNAL_QUEUES = new HashSet<>();
	private final AmqpAdmin amqpAdmin;

	static {
		INTERNAL_QUEUES.add(ServerConstants.REGISTER_QUEUE_NAME);
		INTERNAL_QUEUES.add(ServerConstants.CONFIRM_QUEUE_NAME);
	}

	public RabbitComponentManager(AmqpAdmin amqpAdmin) {
		this.amqpAdmin = amqpAdmin;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		declareInternalQueues();
	}

	public void declareDirectQueue(String queueName) {
		DirectExchange exchange = new DirectExchange(queueName, Boolean.TRUE, Boolean.FALSE, null);
		amqpAdmin.declareExchange(exchange);
		Queue queue = new Queue(queueName, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, null);
		amqpAdmin.declareQueue(queue);
		amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).withQueueName());
	}

	private void declareInternalQueues() {
		if (!INTERNAL_QUEUES.isEmpty()) {
			for (String queue : INTERNAL_QUEUES) {
				declareDirectQueue(queue);
			}
		}
	}
}
