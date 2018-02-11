package org.throwable.exception;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 18:41
 */
public class HandleMessageFailException extends RuntimeException {

    public HandleMessageFailException(String message) {
        super(message);
    }

    public HandleMessageFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandleMessageFailException(Throwable cause) {
        super(cause);
    }
}
