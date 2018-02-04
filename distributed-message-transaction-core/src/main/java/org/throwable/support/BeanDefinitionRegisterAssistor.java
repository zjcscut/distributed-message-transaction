package org.throwable.support;

import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.throwable.utils.AssertUtils;
import org.throwable.utils.ExtendClassUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/11/17 0:35
 */
public enum BeanDefinitionRegisterAssistor {

    INSTANCE;

    private static final Set<String> AVAILABLE_SCOPES = new HashSet<>();

    static {
        AVAILABLE_SCOPES.add(ConfigurableBeanFactory.SCOPE_SINGLETON);
        AVAILABLE_SCOPES.add(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
    }

    public void registerBeanDefinitionFromCommonClass(Class<?> targetClass,
                                                      BeanDefinitionRegistry registry) {
        registerBeanDefinitionFromCommonClass(targetClass, registry, ConfigurableBeanFactory.SCOPE_SINGLETON);
    }

    public void registerBeanDefinitionFromCommonClass(Class<?> targetClass,
                                                      BeanDefinitionRegistry registry,
                                                      String scope) {
        AssertUtils.INSTANCE.assertThrowRuntimeException(AVAILABLE_SCOPES.contains(scope),
                () -> new IllegalArgumentException(String.format("Invalid scope %s", scope)));
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(targetClass);
        beanDefinition.setScope(scope);
        BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDefinition, ExtendClassUtils.INSTANCE.getShortClassName(targetClass));
        BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, registry);
    }

    public void registerBeanDefinitionFromAnnotatedClass(Class<?> targetClass,
                                                         BeanDefinitionRegistry registry) {
        registerBeanDefinitionFromAnnotatedClass(targetClass, registry, ConfigurableBeanFactory.SCOPE_SINGLETON);
    }

    public void registerBeanDefinitionFromAnnotatedClass(Class<?> targetClass,
                                                         BeanDefinitionRegistry registry,
                                                         String scope) {
        AssertUtils.INSTANCE.assertThrowRuntimeException(AVAILABLE_SCOPES.contains(scope),
                () -> new IllegalArgumentException(String.format("Invalid scope %s", scope)));
        AnnotatedGenericBeanDefinition beanDefinition = new AnnotatedGenericBeanDefinition(targetClass);
        beanDefinition.setScope(scope);
        AnnotationConfigUtils.processCommonDefinitionAnnotations(beanDefinition);
        BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDefinition, ExtendClassUtils.INSTANCE.getShortClassName(targetClass));
        BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, registry);
    }
}
