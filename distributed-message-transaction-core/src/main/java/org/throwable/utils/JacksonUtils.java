package org.throwable.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;
import org.throwable.common.GlobalConstants;
import org.throwable.exception.JsonDeserializeException;
import org.throwable.exception.JsonSerializeException;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 18:50
 */
@SuppressWarnings("unchecked")
public enum JacksonUtils {

    INSTANCE;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.setDateFormat(new SimpleDateFormat(GlobalConstants.DATETIME_PATTERN));
        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        MAPPER.disable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String toJson(Object value) {
        AssertUtils.INSTANCE.assertThrowRuntimeException(null != value,
                () -> new IllegalArgumentException("Value must be null!"));
        try {
            return MAPPER.writeValueAsString(value);
        } catch (Exception e) {
            throw new JsonSerializeException(e);
        }
    }

    public static <T> T parse(String content, Class<T> clazz) {
        AssertUtils.INSTANCE.assertThrowRuntimeException(StringUtils.hasText(content),
                () -> new IllegalArgumentException("Content must be blank!"));
        try {
            return MAPPER.readValue(content, clazz);
        } catch (Exception e) {
            throw new JsonDeserializeException(e);
        }
    }

    public static <T> List<T> parseList(String content, Class<T> clazz) {
        AssertUtils.INSTANCE.assertThrowRuntimeException(StringUtils.hasText(content),
                () -> new IllegalArgumentException("Content must be blank!"));
        return parse(content, List.class, clazz);
    }

    private static <T> T parse(String jsonString, JavaType javaType) {
        try {
            return (T) MAPPER.readValue(jsonString, javaType);
        } catch (Exception e) {
            throw new JsonDeserializeException(e);
        }
    }

    private static <T> T parse(String jsonString, Class<?> parametrized, Class<?>... parameterClasses) {
        return (T) parse(jsonString, constructParametricType(parametrized, parameterClasses));
    }

    private static JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
        return MAPPER.getTypeFactory().constructParametricType(parametrized, parameterClasses);
    }
}
