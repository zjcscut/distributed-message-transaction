package org.throwable.client.support;

import org.throwable.client.model.MessageTransaction;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 16:03
 */
public class MessageTransactionBuilder {

    private final MessageTransaction messageTransaction = new MessageTransaction();

    private MessageTransactionBuilder(LocalTransactionExecutor localTransactionExecutor) {
        this.messageTransaction.setLocalTransactionExecutor(localTransactionExecutor);
    }

    public static MessageTransactionBuilder withLocalTransactionExecutor(LocalTransactionExecutor localTransactionExecutor) {
        return new MessageTransactionBuilder(localTransactionExecutor);
    }

    public MessageTransactionBuilder withBusinessSign(String businessSign) {
        messageTransaction.setBusinessSign(businessSign);
        return this;
    }

    public MessageTransactionBuilder withTriggerTimeoutSecond(Integer triggerTimeoutSecond) {
        messageTransaction.setTriggerTransactionTimeoutSecond(triggerTimeoutSecond);
        return this;
    }

    public MessageTransactionBuilder withExecuteTimeoutSecond(Integer executeTimeoutSecond) {
        messageTransaction.setExecuteTransactionTimeoutSecond(executeTimeoutSecond);
        return this;
    }

    public MessageTransaction build() {
        return messageTransaction;
    }
}
