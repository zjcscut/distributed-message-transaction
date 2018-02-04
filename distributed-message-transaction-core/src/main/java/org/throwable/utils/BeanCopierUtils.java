package org.throwable.utils;

import net.sf.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/11/17 0:12
 */
public enum BeanCopierUtils {

    INSTANCE;

    private static final Map<String, BeanCopier> BEAN_COPIER_CACHE = new HashMap<>();

    public <S, T> T copy(S source, Class<T> targetClass) throws Exception {
        AssertUtils.INSTANCE.assertThrowRuntimeException(null != source, () -> new NullPointerException("Source must not be null!"));
        AssertUtils.INSTANCE.assertThrowRuntimeException(null != targetClass, () -> new NullPointerException("TargetClass must not be null!"));
        return convert(source, targetClass);
    }

    public <S, T> List<T> copyList(List<S> sourceList, Class<T> targetClass) throws Exception {
        AssertUtils.INSTANCE.assertThrowRuntimeException(null != sourceList, () -> new NullPointerException("SourceList must not be null!"));
        AssertUtils.INSTANCE.assertThrowRuntimeException(null != targetClass, () -> new NullPointerException("TargetClass must not be null!"));
        List<T> targetList = new ArrayList<>();
        if (!sourceList.isEmpty()) {
            for (S source : sourceList) {
                targetList.add(convert(source, targetClass));
            }
        }
        return targetList;
    }

    private <S, T> T convert(S source, Class<T> targetClass) throws Exception {
        T t = targetClass.newInstance();
        getOrCreateBeanCopier(source.getClass(), targetClass).copy(source, t, null);
        return t;
    }

    private BeanCopier getOrCreateBeanCopier(Class<?> source, Class<?> target) {
        String cacheKey = generateCacheKey(source, target);
        BeanCopier beanCopier;
        if (BEAN_COPIER_CACHE.containsKey(cacheKey)) {
            beanCopier = BEAN_COPIER_CACHE.get(cacheKey);
        } else {
            beanCopier = BeanCopier.create(source, target, false);
            BEAN_COPIER_CACHE.put(cacheKey, beanCopier);
        }
        return beanCopier;
    }

    private String generateCacheKey(Class<?> source, Class<?> target) {
        return String.format("%s_%s", source.getName(), target.getName());
    }
}
