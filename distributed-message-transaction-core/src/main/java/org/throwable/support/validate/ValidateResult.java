package org.throwable.support.validate;

import lombok.Data;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/5 16:20
 */
@Data
public class ValidateResult<T> {

    private Boolean pass;
    private T target;
    private Set<ConstraintViolation<T>> constraintViolations;
}
