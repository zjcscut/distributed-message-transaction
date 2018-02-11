package org.throwable.client.common;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 15:13
 */
public enum LocalTransactionStatusEnum {

    /**
     * 终态：本地事务执行成功
     */
    SUCCESS,

    /**
     * 终态：本地事务回滚
     */
    ROLLBACK
}
