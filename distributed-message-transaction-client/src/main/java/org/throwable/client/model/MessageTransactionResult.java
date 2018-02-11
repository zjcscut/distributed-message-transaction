package org.throwable.client.model;

import lombok.Data;
import org.throwable.client.common.LocalTransactionStatusEnum;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 15:42
 */
@Data
public class MessageTransactionResult {

    private LocalTransactionStatusEnum transactionStatus;
    private String transactionId;
    private String businessSign;
}
