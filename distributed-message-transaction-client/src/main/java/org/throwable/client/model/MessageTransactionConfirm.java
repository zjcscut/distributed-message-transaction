package org.throwable.client.model;

import lombok.Data;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 18:44
 */
@Data
public class MessageTransactionConfirm {

    private String transactionId;
    private Integer transactionStatus;
}
