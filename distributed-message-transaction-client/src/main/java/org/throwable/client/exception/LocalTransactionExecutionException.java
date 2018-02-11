package org.throwable.client.exception;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 15:28
 */
public class LocalTransactionExecutionException extends RuntimeException {

    public LocalTransactionExecutionException(String message) {
        super(message);
    }

    public LocalTransactionExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocalTransactionExecutionException(Throwable cause) {
        super(cause);
    }
}
