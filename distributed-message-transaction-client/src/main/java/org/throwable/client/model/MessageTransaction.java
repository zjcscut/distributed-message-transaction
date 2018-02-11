package org.throwable.client.model;

import lombok.Data;
import org.throwable.client.support.LocalTransactionChecker;
import org.throwable.client.support.LocalTransactionExecutor;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 16:00
 */
@Data
public class MessageTransaction {

    private String businessSign;
    private Integer triggerTransactionTimeoutSecond;
    private Integer executeTransactionTimeoutSecond;
    private LocalTransactionExecutor localTransactionExecutor;
    private Class<? extends LocalTransactionChecker> localTransactionCheckerClass;
    private List<DestinationMessage> destinationMessages;
    private ExecutorService executorService;
}
