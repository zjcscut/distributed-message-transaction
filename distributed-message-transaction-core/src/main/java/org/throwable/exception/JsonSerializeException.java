package org.throwable.exception;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 18:51
 */
public class JsonSerializeException extends RuntimeException {

    public JsonSerializeException(String message) {
        super(message);
    }

    public JsonSerializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonSerializeException(Throwable cause) {
        super(cause);
    }
}
