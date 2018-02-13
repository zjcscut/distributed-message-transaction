package org.throwable.client.support;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 17:44
 */
public class ClientRabbitComponentManager implements InitializingBean {

    private final AmqpAdmin amqpAdmin;
    private final ClientRabbitQueueExtractor clientRabbitQueueExtractor;
    private final RabbitTemplate rabbitTemplate;

    public ClientRabbitComponentManager(AmqpAdmin amqpAdmin,
                                        ClientRabbitQueueExtractor clientRabbitQueueExtractor,
                                        RabbitTemplate rabbitTemplate) {
        this.amqpAdmin = amqpAdmin;
        this.clientRabbitQueueExtractor = clientRabbitQueueExtractor;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        declareQueues();
    }

    private void declareQueues() {
        declareQueue(clientRabbitQueueExtractor.getTriggerQueue());
        declareQueue(clientRabbitQueueExtractor.getCheckQueue());
    }

    private void declareQueue(String queueName) {
        DirectExchange exchange = new DirectExchange(queueName, Boolean.TRUE, Boolean.FALSE, null);
        amqpAdmin.declareExchange(exchange);
        Queue queue = new Queue(queueName, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, null);
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).withQueueName());
    }
}
