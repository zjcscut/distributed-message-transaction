package org.throwable.client.support;

import lombok.extern.slf4j.Slf4j;
import org.throwable.client.common.LocalTransactionStatusEnum;

/**
 * @author throwable
 * @version v1.0
 * @description adapter,to catch all exceptions in transaction and convert to rollback status
 * @since 2018/2/11 15:15
 */
@Slf4j
public class LocalTransactionExecutorAdapter {

    private final LocalTransactionExecutor delegate;

    public LocalTransactionExecutorAdapter(LocalTransactionExecutor delegate) {
        this.delegate = delegate;
    }

    public LocalTransactionStatusEnum doInLocalTransaction() {
        LocalTransactionStatusEnum localTransactionStats;
        try {
            localTransactionStats = delegate.doInLocalTransaction();
        } catch (Exception e) {
            log.error("Process doInLocalTransaction encounter an exception,local transaction rollback", e);
            return LocalTransactionStatusEnum.ROLLBACK;
        }
        return localTransactionStats;
    }
}
