package org.throwable.support.validate;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/5 16:13
 */
public interface ValidateFailStrategy {

    /**
     * 处理校验目标和结果
     *
     * @param target               target
     * @param constraintViolations constraintViolations
     * @param <T>                  <T>
     */
    <T> void handleValidation(T target, Set<ConstraintViolation<T>> constraintViolations);
}
