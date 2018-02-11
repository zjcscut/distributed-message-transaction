package org.throwable.client.support.listener;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.StringUtils;
import org.throwable.client.common.LocalTransactionStatusEnum;
import org.throwable.client.common.ServerTransactionStatusEnum;
import org.throwable.client.model.CheckLocalTransactionCallback;
import org.throwable.client.model.MessageTransactionConfirm;
import org.throwable.client.support.ClientRabbitQueueExtractor;
import org.throwable.client.support.LocalTransactionChecker;
import org.throwable.common.CommonConstants;
import org.throwable.common.RabbitConstants;
import org.throwable.support.AbstractMessageListener;
import org.throwable.support.ConfigurableListener;
import org.throwable.utils.JacksonUtils;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 18:33
 */
public class LocalTransactionCheckListener extends AbstractMessageListener<CheckLocalTransactionCallback>
        implements BeanFactoryAware, ConfigurableListener {

    private DefaultListableBeanFactory beanFactory;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ClientRabbitQueueExtractor clientRabbitQueueExtractor;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    protected void handleMessage(CheckLocalTransactionCallback checkLocalTransactionCallback) throws Exception {
        String checkerClassName = checkLocalTransactionCallback.getCheckerClassName();
        if (StringUtils.hasText(checkerClassName)) {
            @SuppressWarnings("unchecked")
            Class<? extends LocalTransactionChecker> checkerClazz = (Class<? extends LocalTransactionChecker>) Class.forName(checkerClassName);
            LocalTransactionChecker transactionChecker = beanFactory.getBean(checkerClazz);
            LocalTransactionStatusEnum localTransactionStatus = transactionChecker.checkLocalTransaction(checkLocalTransactionCallback);
            MessageTransactionConfirm confirm = new MessageTransactionConfirm();
            confirm.setTransactionId(checkLocalTransactionCallback.getTransactionId());
            confirm.setTransactionStatus(ServerTransactionStatusEnum.fromLocalTransactionStatus(localTransactionStatus));
            rabbitTemplate.send(RabbitConstants.CONFIRM_EXCHANGE_NAME, RabbitConstants.CONFIRM_ROUTING_KEY,
                    MessageBuilder.withBody(JacksonUtils.INSTANCE.toJson(confirm).getBytes(CommonConstants.CHARSET)).build());
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
        return 5;
    }

    @Override
    public Integer getMaxConcurrentConsumerNumber() {
        return 10;
    }
}
