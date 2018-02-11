package org.throwable.server.support.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.throwable.common.RabbitConstants;
import org.throwable.server.configuration.BaseServerProperties;
import org.throwable.server.event.MessageTransactionEventPublisher;
import org.throwable.server.event.category.TransactionConfirmEvent;
import org.throwable.server.model.entity.MessageLog;
import org.throwable.server.model.vo.MessageTransactionConfirmVO;
import org.throwable.server.service.MessageTransactionService;
import org.throwable.support.AbstractMessageListener;
import org.throwable.support.ConfigurableListener;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 19:07
 */
@Slf4j
public class MessageTransactionConfirmListener extends AbstractMessageListener<MessageTransactionConfirmVO> implements ConfigurableListener {

    @Autowired
    private BaseServerProperties baseServerProperties;

    @Autowired
    private MessageTransactionService messageTransactionService;

    @Autowired
    private MessageTransactionEventPublisher messageTransactionEventPublisher;

    @Override
    protected void handleMessage(MessageTransactionConfirmVO messageTransactionConfirmVO) {
        MessageLog messageLog = messageTransactionService.confirmMessageTransaction(messageTransactionConfirmVO);
        TransactionConfirmEvent event = new TransactionConfirmEvent(getClass().getName());
        event.setMessageLog(messageLog);
        messageTransactionEventPublisher.publish(event);
    }

    @Override
    public String getTargetQueueName() {
        return RabbitConstants.CONFIRM_QUEUE_NAME;
    }

    @Override
    public AcknowledgeMode getAcknowledgeMode() {
        return AcknowledgeMode.NONE;
    }

    @Override
    public Integer getConcurrentConsumerNumber() {
        return baseServerProperties.getConfirmConcurrentConsumerNumber();
    }

    @Override
    public Integer getMaxConcurrentConsumerNumber() {
        return baseServerProperties.getConfirmMaxConcurrentConsumerNumber();
    }
}
