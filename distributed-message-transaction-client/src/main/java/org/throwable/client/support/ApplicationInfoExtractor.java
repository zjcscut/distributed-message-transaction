package org.throwable.client.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.throwable.client.configuration.BaseClientProperties;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 17:12
 */
public class ApplicationInfoExtractor implements EnvironmentAware {

	private String applicationName;
	private String instanceSign;

	@Autowired
	private BaseClientProperties baseClientProperties;

	@Override
	public void setEnvironment(Environment environment) {
		applicationName = environment.getProperty("spring.application.name");
		Assert.hasText(applicationName, "spring.application.name must not be null");
		instanceSign = environment.getProperty("dmt.client.base.instance-sign");
		if (null == instanceSign) {
			instanceSign = baseClientProperties.getInstanceSign();
		}
		Assert.hasText(instanceSign, "instanceSign must not be null");
	}

	public String extractApplicationName() {
		return applicationName;
	}

	public String extractInstanceSign() {
		return instanceSign;
	}
}
