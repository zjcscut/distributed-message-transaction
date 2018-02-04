package org.throwable.exception;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 18:51
 */
public class JsonDeserializeException extends RuntimeException {

    public JsonDeserializeException(String message) {
        super(message);
    }

    public JsonDeserializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonDeserializeException(Throwable cause) {
        super(cause);
    }
}
