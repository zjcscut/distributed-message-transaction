package org.throwable.server.model.entity;

import org.throwable.server.common.TransactionStatusEnum;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/2 15:46
 */
public class MessageLog {

    private Long id;

    private Integer transactionStatus;

    public void setTransactionStatusEnum(TransactionStatusEnum transactionStatusEnum) {
        this.transactionStatus = transactionStatusEnum.ordinal();
    }

    public TransactionStatusEnum getTransactionStatusEnum() {
        for (TransactionStatusEnum status : TransactionStatusEnum.values()) {
            if (null != this.transactionStatus && status.ordinal() == this.transactionStatus) {
                return status;
            }
        }
        return null;
    }
}
