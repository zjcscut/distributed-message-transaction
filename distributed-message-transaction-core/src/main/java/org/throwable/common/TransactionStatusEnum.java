package org.throwable.common;

/**
 * @author throwable
 * @version v1.0
 * @description 事务状态枚举
 * @since 2018/2/2 18:25
 */
public enum TransactionStatusEnum {

    /**
     * 状态丢失预留值
     */
    NONE,

    /**
     * 初始状态：已注册
     */
    REGISTERED,

    /**
     * 终态：本地事务执行成功
     */
    SUCCESS,

    /**
     * 终态：本地事务回滚
     */
    ROLLBACK,

    /**
     * 失败,原因一般是错失触发队列
     */
    FAIL;

    public static TransactionStatusEnum fromValue(Integer value) {
        for (TransactionStatusEnum statusEnum : TransactionStatusEnum.values()) {
            if (statusEnum.ordinal() == value) {
                return statusEnum;
            }
        }
        return TransactionStatusEnum.NONE;
    }

    public static boolean isTerminal(TransactionStatusEnum status) {
        return null != status && (TransactionStatusEnum.SUCCESS.equals(status) || TransactionStatusEnum.ROLLBACK.equals(status));
    }

    public static boolean isTerminal(Integer value) {
        return isTerminal(fromValue(value));
    }
}
