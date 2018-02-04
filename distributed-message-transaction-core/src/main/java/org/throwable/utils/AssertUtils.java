package org.throwable.utils;

import java.util.function.Supplier;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/11/17 18:13
 */
public enum AssertUtils {

    INSTANCE;

    public void assertThrowRuntimeException(boolean expression,
                                            Supplier<? extends RuntimeException> runtimeExceptionSupplier) {
        if (!expression && null != runtimeExceptionSupplier) {
            throw runtimeExceptionSupplier.get();
        }
    }
}
