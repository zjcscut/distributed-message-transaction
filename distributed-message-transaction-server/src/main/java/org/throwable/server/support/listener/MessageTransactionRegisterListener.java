package org.throwable.server.support.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.throwable.server.common.ServerConstants;
import org.throwable.server.configuration.BaseServerProperties;
import org.throwable.server.model.vo.MessageTransactionRegisterVO;
import org.throwable.server.support.ConfigurableListener;

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

	@Override
	protected void handleMessage(MessageTransactionRegisterVO messageTransactionRegisterVO) {
		log.info(messageTransactionRegisterVO.toString());
	}

	@Override
	public String getTargetQueueName() {
		return ServerConstants.REGISTER_QUEUE_NAME;
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
