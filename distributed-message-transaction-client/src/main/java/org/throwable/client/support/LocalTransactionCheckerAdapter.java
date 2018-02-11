package org.throwable.client.support;

import lombok.extern.slf4j.Slf4j;
import org.throwable.client.common.LocalTransactionStatusEnum;
import org.throwable.client.model.CheckLocalTransactionCallback;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 16:18
 */
@Slf4j
public class LocalTransactionCheckerAdapter {

    private final LocalTransactionChecker delegate;

    public LocalTransactionCheckerAdapter(LocalTransactionChecker delegate) {
        this.delegate = delegate;
    }

    public LocalTransactionStatusEnum checkLocalTransaction(CheckLocalTransactionCallback callback) {
        LocalTransactionStatusEnum transactionStatusEnum;
        try {
            transactionStatusEnum = delegate.checkLocalTransaction(callback);
        } catch (Exception e) {
            log.error("Process checkLocalTransaction encounter an exception,local transaction rollback", e);
            return LocalTransactionStatusEnum.ROLLBACK;
        }
        return transactionStatusEnum;
    }
}
