package org.throwable.server.exception;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 17:43
 */
public class ListenerBeanNotFoundException extends RuntimeException{

    public ListenerBeanNotFoundException(String message) {
        super(message);
    }

    public ListenerBeanNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ListenerBeanNotFoundException(Throwable cause) {
        super(cause);
    }
}
