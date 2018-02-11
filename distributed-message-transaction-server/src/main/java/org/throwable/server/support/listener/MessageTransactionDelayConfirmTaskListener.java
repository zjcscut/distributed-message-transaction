package org.throwable.server.support.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.throwable.server.common.ServerConstants;
import org.throwable.server.configuration.BaseServerProperties;
import org.throwable.server.event.MessageTransactionEventPublisher;
import org.throwable.server.event.category.TransactionDelayConfirmEvent;
import org.throwable.server.model.vo.MessageTransactionDelayConfirmVO;
import org.throwable.support.AbstractMessageListener;
import org.throwable.support.ConfigurableListener;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/5 11:47
 */
@Slf4j
public class MessageTransactionDelayConfirmTaskListener extends AbstractMessageListener<MessageTransactionDelayConfirmVO>
        implements ConfigurableListener {

    @Autowired
    private BaseServerProperties baseServerProperties;

    @Autowired
    private MessageTransactionEventPublisher messageTransactionEventPublisher;

    @Override
    protected void handleMessage(MessageTransactionDelayConfirmVO messageTransactionDelayConfirmVO) {
        TransactionDelayConfirmEvent event = new TransactionDelayConfirmEvent(getClass().getName());
        event.setId(messageTransactionDelayConfirmVO.getId());
        messageTransactionEventPublisher.publish(event);
    }

    @Override
    public String getTargetQueueName() {
        return ServerConstants.DELAY_CONFIRM_TASK_QUEUE;
    }

    @Override
    public AcknowledgeMode getAcknowledgeMode() {
        return AcknowledgeMode.NONE;
    }

    @Override
    public Integer getConcurrentConsumerNumber() {
        return baseServerProperties.getDelayConfirmConcurrentConsumerNumber();
    }

    @Override
    public Integer getMaxConcurrentConsumerNumber() {
        return baseServerProperties.getDelayConfirmMaxConcurrentConsumerNumber();
    }
}
