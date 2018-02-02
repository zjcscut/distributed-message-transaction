package org.throwable.server.common;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/2 18:25
 */
public enum TransactionStatusEnum {

    REGISTERED,

    SUCCESS,

    ROLLBACK;

    public static TransactionStatusEnum fromValue(Integer value) {
        for (TransactionStatusEnum statusEnum : TransactionStatusEnum.values()) {
            if (statusEnum.ordinal() == value) {
                return statusEnum;
            }
        }
        return null;
    }

    public static boolean isTerminal(TransactionStatusEnum status) {
        return null != status && (TransactionStatusEnum.SUCCESS.equals(status) || TransactionStatusEnum.ROLLBACK.equals(status));
    }

    public static boolean isTerminal(Integer value) {
        return isTerminal(fromValue(value));
    }
}
