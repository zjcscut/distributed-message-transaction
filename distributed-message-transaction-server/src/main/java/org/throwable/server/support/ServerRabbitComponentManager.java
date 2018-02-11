package org.throwable.server.support;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.throwable.common.RabbitConstants;
import org.throwable.server.common.ServerConstants;
import org.throwable.server.configuration.BaseServerProperties;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/5 0:09
 */
public class ServerRabbitComponentManager implements InitializingBean {

    private static final Set<String> INTERNAL_QUEUES = new HashSet<>();
    private final AmqpAdmin amqpAdmin;

    @Autowired
    private BaseServerProperties baseServerProperties;

    static {
        INTERNAL_QUEUES.add(RabbitConstants.REGISTER_QUEUE_NAME);
        INTERNAL_QUEUES.add(RabbitConstants.CONFIRM_QUEUE_NAME);
    }

    public ServerRabbitComponentManager(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        declareInternalDlxQueues();
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

    private void declareInternalDlxQueues() {
        DirectExchange dlxLetterExchange = new DirectExchange(ServerConstants.DELAY_CONFIRM_DEAD_LETTER_QUEUE,
                Boolean.TRUE, Boolean.FALSE, null);
        amqpAdmin.declareExchange(dlxLetterExchange);
        Map<String, Object> dlxLetterArgs = new HashMap<>(3);
        dlxLetterArgs.put("x-message-ttl", baseServerProperties.getConfirmIntervalMillisecond());
        dlxLetterArgs.put("x-dead-letter-exchange", ServerConstants.DELAY_CONFIRM_TASK_QUEUE);
        dlxLetterArgs.put("x-dead-letter-routing-key", ServerConstants.DELAY_CONFIRM_TASK_QUEUE);
        Queue dlxLetterQueue = new Queue(ServerConstants.DELAY_CONFIRM_DEAD_LETTER_QUEUE, Boolean.TRUE,
                Boolean.FALSE, Boolean.FALSE, dlxLetterArgs);
        amqpAdmin.declareQueue(dlxLetterQueue);
        amqpAdmin.declareBinding(BindingBuilder.bind(dlxLetterQueue).to(dlxLetterExchange).withQueueName());
        DirectExchange dlxTaskExchange = new DirectExchange(ServerConstants.DELAY_CONFIRM_TASK_QUEUE,
                Boolean.TRUE, Boolean.FALSE, null);
        amqpAdmin.declareExchange(dlxTaskExchange);
        Queue dlxTaskQueue = new Queue(ServerConstants.DELAY_CONFIRM_TASK_QUEUE, Boolean.TRUE,
                Boolean.FALSE, Boolean.FALSE, null);
        amqpAdmin.declareQueue(dlxTaskQueue);
        amqpAdmin.declareBinding(BindingBuilder.bind(dlxTaskQueue).to(dlxTaskExchange).withQueueName());
    }
}
