package org.throwable.client.support;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.throwable.client.configuration.BaseClientProperties;
import org.throwable.client.model.ConfirmLocalTransactionCallback;
import org.throwable.support.AbstractMessageListener;
import org.throwable.support.ConfigurableListener;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 18:34
 */
public class LocalTransactionTriggerListener extends AbstractMessageListener<ConfirmLocalTransactionCallback>
		implements ConfigurableListener {

	@Autowired
	private ClientRabbitQueueExtractor clientRabbitQueueExtractor;

	@Autowired
	private BaseClientProperties baseClientProperties;

	@Override
	protected void handleMessage(ConfirmLocalTransactionCallback callback) throws Exception {
		if (LocalTransactionExecutionSynchronizer.existTransactionConsumer(callback.getTransactionId())) {
			if (LocalTransactionExecutionSynchronizer.existTransactionExecutor(callback.getTransactionId())) {
				BlockingLocalTransactionExecutorConsumer transactionConsumer
						= LocalTransactionExecutionSynchronizer.getTransactionConsumer(callback.getTransactionId());
				LocalTransactionExecutorAdapter transactionExecutor
						= LocalTransactionExecutionSynchronizer.getTransactionExecutor(callback.getTransactionId());
				transactionConsumer.addLocalTransactionExecutor(transactionExecutor);
			}
		}
	}

	@Override
	public String getTargetQueueName() {
		return clientRabbitQueueExtractor.getTriggerQueue();
	}

	@Override
	public AcknowledgeMode getAcknowledgeMode() {
		return AcknowledgeMode.NONE;
	}

	@Override
	public Integer getConcurrentConsumerNumber() {
		return baseClientProperties.getTriggerConcurrentConsumerNumber();
	}

	@Override
	public Integer getMaxConcurrentConsumerNumber() {
		return baseClientProperties.getTriggerMaxConcurrentConsumerNumber();
	}
}
