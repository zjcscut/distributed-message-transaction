package org.throwable.client.exception;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 17:00
 */
public class LocalTransactionUnknownExecutionException extends RuntimeException {

    public LocalTransactionUnknownExecutionException(String message) {
        super(message);
    }

    public LocalTransactionUnknownExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocalTransactionUnknownExecutionException(Throwable cause) {
        super(cause);
    }
}
