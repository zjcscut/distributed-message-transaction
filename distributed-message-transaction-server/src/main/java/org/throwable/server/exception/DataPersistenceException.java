package org.throwable.server.exception;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/5 16:49
 */
public class DataPersistenceException extends RuntimeException{

    public DataPersistenceException(String message) {
        super(message);
    }

    public DataPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataPersistenceException(Throwable cause) {
        super(cause);
    }
}
