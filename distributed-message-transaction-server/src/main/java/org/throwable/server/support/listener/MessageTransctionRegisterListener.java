package org.throwable.server.support.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.throwable.server.model.vo.MessageTransctionRegisterVO;
import org.throwable.server.support.ConfigurableListener;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 19:07
 */
@Slf4j
public class MessageTransctionRegisterListener extends AbstractMessageListener<MessageTransctionRegisterVO> implements ConfigurableListener {

    @Override
    protected void handleMessage(MessageTransctionRegisterVO messageTransctionRegisterVO) {
        log.info(messageTransctionRegisterVO.toString());
    }

    @Override
    public String getTargetQueueName() {
        return "test-dmt";
    }

    @Override
    public AcknowledgeMode getAcknowledgeMode() {
        return AcknowledgeMode.NONE;
    }

    @Override
    public Integer getConcurrentConsumerNumber() {
        return 1;
    }

    @Override
    public Integer getMaxConcurrentConsumerNumber() {
        return 1;
    }
}
