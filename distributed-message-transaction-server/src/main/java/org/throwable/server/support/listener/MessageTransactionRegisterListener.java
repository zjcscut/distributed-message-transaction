package org.throwable.server.support.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.throwable.common.RabbitConstants;
import org.throwable.server.configuration.BaseServerProperties;
import org.throwable.server.event.MessageTransactionEventPublisher;
import org.throwable.server.event.category.RegisterSuccessEvent;
import org.throwable.server.model.entity.MessageLog;
import org.throwable.server.model.vo.MessageTransactionRegisterVO;
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
public class MessageTransactionRegisterListener extends AbstractMessageListener<MessageTransactionRegisterVO> implements ConfigurableListener {

    @Autowired
    private BaseServerProperties baseServerProperties;

    @Autowired
    private MessageTransactionService messageTransactionService;

    @Autowired
    private MessageTransactionEventPublisher messageTransactionEventPublisher;

    @Override
    protected void handleMessage(MessageTransactionRegisterVO messageTransactionRegisterVO) {
        MessageLog messageLog = messageTransactionService.registerMessageTransaction(messageTransactionRegisterVO);
        RegisterSuccessEvent successEvent = new RegisterSuccessEvent(getClass().getName());
        successEvent.setMessageLog(messageLog);
        messageTransactionEventPublisher.publish(successEvent);
    }

    @Override
    public String getTargetQueueName() {
        return RabbitConstants.REGISTER_QUEUE_NAME;
    }

    @Override
    public AcknowledgeMode getAcknowledgeMode() {
        return AcknowledgeMode.NONE;
    }

    @Override
    public Integer getConcurrentConsumerNumber() {
        return baseServerProperties.getRegisterConcurrentConsumerNumber();
    }

    @Override
    public Integer getMaxConcurrentConsumerNumber() {
        return baseServerProperties.getRegisterMaxConcurrentConsumerNumber();
    }
}
