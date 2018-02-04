package org.throwable.utils;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 19:32
 */
public enum  ExtendClassUtils {

    INSTANCE;

    public String getShortClassName(Class<?> targetClass){
        String shortName = targetClass.getSimpleName();
        return String.format("%s%s", shortName.substring(0, 1).toLowerCase(), shortName.substring(1));
    }
}
