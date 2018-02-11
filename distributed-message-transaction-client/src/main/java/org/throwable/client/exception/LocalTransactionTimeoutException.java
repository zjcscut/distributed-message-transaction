package org.throwable.client.exception;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 15:50
 */
public class LocalTransactionTimeoutException extends RuntimeException {

    public LocalTransactionTimeoutException(String message) {
        super(message);
    }

    public LocalTransactionTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocalTransactionTimeoutException(Throwable cause) {
        super(cause);
    }
}
