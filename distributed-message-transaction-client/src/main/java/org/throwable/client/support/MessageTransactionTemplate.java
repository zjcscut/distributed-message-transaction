package org.throwable.client.support;

import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.throwable.client.configuration.BaseClientProperties;
import org.throwable.client.model.ExecutorConsumerParameter;
import org.throwable.client.model.InternalMessageTransaction;
import org.throwable.client.model.MessageTransaction;
import org.throwable.client.model.MessageTransactionResult;
import org.throwable.common.CommonConstants;
import org.throwable.common.RabbitConstants;
import org.throwable.utils.JacksonUtils;

import java.util.concurrent.ExecutorService;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 15:39
 */
public class MessageTransactionTemplate {

    private static final MessageTransactionValidator VALIDATOR = new MessageTransactionValidator();
    private static final MessageTransactionConverter CONVERTER = new MessageTransactionConverter();

    @Autowired
    private BaseClientProperties baseClientProperties;

    @Autowired
    private MessageTransactionThreadPoolProvider messageTransactionThreadPoolProvider;

    @Autowired
    private ApplicationInfoExtractor applicationInfoExtractor;

    @Autowired
    private ClientRabbitQueueExtractor clientRabbitQueueExtractor;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public MessageTransactionResult processMessageTransaction(MessageTransaction messageTransaction) throws Exception {
        VALIDATOR.validate(messageTransaction);
        InternalMessageTransaction transaction = CONVERTER.convert(messageTransaction);
        wrapInternalMessageTransaction(transaction);
        try {
            ExecutorConsumerParameter parameter = new ExecutorConsumerParameter();
            parameter.setTransactionId(transaction.getTransactionId());
            parameter.setBusinessSign(transaction.getBusinessSign());
            parameter.setExecuteTransactionTimeoutSecond(determineExecuteTransactionTimeoutSecond(messageTransaction.getExecuteTransactionTimeoutSecond()));
            parameter.setTriggerTransactionTimeoutSecond(determineTriggerTransactionTimeoutSecond(messageTransaction.getTriggerTransactionTimeoutSecond()));
            parameter.setExecutorService(determineExecutorService(messageTransaction.getExecutorService()));
            BlockingLocalTransactionExecutorConsumer consumer = new BlockingLocalTransactionExecutorConsumer(parameter);
            LocalTransactionExecutionSynchronizer.addTransactionConsumer(transaction.getTransactionId(), consumer);
            LocalTransactionExecutorAdapter executorAdapter = new LocalTransactionExecutorAdapter(messageTransaction.getLocalTransactionExecutor());
            LocalTransactionExecutionSynchronizer.addTransactionExecutor(transaction.getTransactionId(), executorAdapter);
            //发送注册事务消息
            rabbitTemplate.send(RabbitConstants.REGISTER_EXCHANGE_NAME, RabbitConstants.REGISTER_ROUTING_KEY,
                    MessageBuilder.withBody(JacksonUtils.INSTANCE.toJson(transaction).getBytes(CommonConstants.CHARSET)).build());
            //阻塞等待回调触发
            return consumer.processLocalTransactionExecutor();
        } finally {
            LocalTransactionExecutionSynchronizer.removeTransactionConsumer(transaction.getTransactionId());
            LocalTransactionExecutionSynchronizer.removeTransactionExecutor(transaction.getTransactionId());
        }
    }

    private Integer determineTriggerTransactionTimeoutSecond(Integer triggerTransactionTimeoutSecond) {
        return null != triggerTransactionTimeoutSecond ? triggerTransactionTimeoutSecond : baseClientProperties.getTriggerTransactionTimeoutSecond();
    }

    private Integer determineExecuteTransactionTimeoutSecond(Integer executeTransactionTimeoutSecond) {
        return null != executeTransactionTimeoutSecond ? executeTransactionTimeoutSecond : baseClientProperties.getExecuteTransactionTimeoutSecond();
    }

    private ExecutorService determineExecutorService(ExecutorService executorService) {
        return null != executorService ? executorService : messageTransactionThreadPoolProvider.getTransactionExecutor();
    }

    private void wrapInternalMessageTransaction(InternalMessageTransaction transaction) {
        transaction.setApplicationName(applicationInfoExtractor.extractApplicationName());
        transaction.setCheckQueue(clientRabbitQueueExtractor.getCheckQueue());
    }
}
