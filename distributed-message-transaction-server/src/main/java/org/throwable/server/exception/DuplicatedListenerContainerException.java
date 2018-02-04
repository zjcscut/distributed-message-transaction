package org.throwable.server.exception;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 16:46
 */
public class DuplicatedListenerContainerException extends RuntimeException {

    public DuplicatedListenerContainerException(String message) {
        super(message);
    }

    public DuplicatedListenerContainerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedListenerContainerException(Throwable cause) {
        super(cause);
    }
}
