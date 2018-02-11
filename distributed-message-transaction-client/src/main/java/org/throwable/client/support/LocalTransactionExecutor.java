package org.throwable.client.support;


import org.throwable.client.common.LocalTransactionStatusEnum;

/**
 * @author throwable
 * @version v1.0
 * @description Local transaction executor
 * @since 2018/2/11 9:28
 */
public interface LocalTransactionExecutor {

    /**
     * 执行本地事务
     *
     * @return LocalTransactionStatusEnum
     */
    LocalTransactionStatusEnum doInLocalTransaction();
}
