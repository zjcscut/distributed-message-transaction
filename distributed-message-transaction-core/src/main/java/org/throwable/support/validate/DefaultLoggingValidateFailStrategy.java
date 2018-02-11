package org.throwable.support.validate;

import lombok.extern.slf4j.Slf4j;
import org.throwable.utils.JacksonUtils;

import javax.validation.ConstraintViolation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/5 16:24
 */
@Slf4j
public class DefaultLoggingValidateFailStrategy implements ValidateFailStrategy {

    @Override
    public <T> void handleValidation(T target, Set<ConstraintViolation<T>> constraintViolations) {
        StringBuilder logContent = new StringBuilder("Validate entity failed,target = ")
                .append(JacksonUtils.INSTANCE.toJson(target))
                .append(",result = ");
        if (null != constraintViolations) {
            Map<String, String> result = new HashMap<>(constraintViolations.size());
            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                result.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
            }
            logContent.append(JacksonUtils.INSTANCE.toJson(result));
        }
        log.error(logContent.toString());
    }
}
