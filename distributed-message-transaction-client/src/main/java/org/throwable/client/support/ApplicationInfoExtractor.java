package org.throwable.client.support;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 17:12
 */
public class ApplicationInfoExtractor implements EnvironmentAware{

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public String extractApplicationName(){
        return environment.getProperty("spring.application.name");
    }
}
