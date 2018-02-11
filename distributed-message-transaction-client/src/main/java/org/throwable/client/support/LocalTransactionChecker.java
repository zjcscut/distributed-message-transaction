package org.throwable.client.support;

import org.throwable.client.common.LocalTransactionStatusEnum;
import org.throwable.client.model.CheckLocalTransactionCallback;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 16:13
 */
public interface LocalTransactionChecker {

    /**
     * 检查本地事务
     *
     * @return LocalTransactionStatusEnum
     */
    LocalTransactionStatusEnum checkLocalTransaction(CheckLocalTransactionCallback callback);
}
