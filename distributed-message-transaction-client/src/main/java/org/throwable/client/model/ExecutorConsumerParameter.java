package org.throwable.client.model;

import lombok.Data;

import java.util.concurrent.ExecutorService;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 16:33
 */
@Data
public class ExecutorConsumerParameter {

    private String transactionId;
    private String businessSign;
    private Integer triggerTransactionTimeoutSecond;
    private Integer executeTransactionTimeoutSecond;
    private ExecutorService executorService;
}
