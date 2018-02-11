package org.throwable.client.exception;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 18:08
 */
public class QueueAlreadyExistException extends RuntimeException{

    public QueueAlreadyExistException(String message) {
        super(message);
    }

    public QueueAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public QueueAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
