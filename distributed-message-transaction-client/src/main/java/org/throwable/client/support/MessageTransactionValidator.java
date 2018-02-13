package org.throwable.client.support;

import org.springframework.util.Assert;
import org.throwable.client.model.MessageTransaction;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 16:28
 */
public class MessageTransactionValidator {

    public void validate(MessageTransaction messageTransaction){
		Assert.notNull(messageTransaction,"messageTransaction must not be null");
		Assert.hasText(messageTransaction.getBusinessSign(),"businessSign must not be null");
		Assert.notNull(messageTransaction.getLocalTransactionExecutor(),"localTransactionExecutor must not be null");
		Assert.notEmpty(messageTransaction.getDestinationMessages(),"destinationMessages must not be empty");
    }
}
