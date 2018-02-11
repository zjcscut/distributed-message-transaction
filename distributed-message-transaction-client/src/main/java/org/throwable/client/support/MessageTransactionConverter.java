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
        return transaction;
    }
}
