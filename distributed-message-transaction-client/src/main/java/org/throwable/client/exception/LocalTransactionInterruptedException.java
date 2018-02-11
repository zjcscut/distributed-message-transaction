package org.throwable.client.exception;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 15:48
 */
public class LocalTransactionInterruptedException extends RuntimeException {

    public LocalTransactionInterruptedException(String message) {
        super(message);
    }

    public LocalTransactionInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocalTransactionInterruptedException(Throwable cause) {
        super(cause);
    }
}
