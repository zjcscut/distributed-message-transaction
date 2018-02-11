package org.throwable.client.support;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 17:12
 */
public class ApplicationInfoExtractor implements EnvironmentAware {

    private String applicationName;

    @Override
    public void setEnvironment(Environment environment) {
        applicationName = environment.getProperty("spring.application.name");
        Assert.isTrue(StringUtils.hasText(applicationName), "spring.application.name must not be null");
    }

    public String extractApplicationName() {
        return applicationName;
    }
}
