package org.throwable.client.support;

import org.throwable.client.model.InternalMessageTransaction;
import org.throwable.client.model.MessageTransaction;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 16:26
 */
public class MessageTransactionConverter {

	public InternalMessageTransaction convert(MessageTransaction messageTransaction) {
		InternalMessageTransaction transaction = new InternalMessageTransaction();
		transaction.setBusinessSign(messageTransaction.getBusinessSign());
		transaction.setPresents(messageTransaction.getDestinationMessages());
		if (null != messageTransaction.getLocalTransactionCheckerClass()) {
			transaction.setCheckerClassName(messageTransaction.getLocalTransactionCheckerClass().getName());
		}
		transaction.setPresents(messageTransaction.getDestinationMessages());
		return transaction;
	}
}
