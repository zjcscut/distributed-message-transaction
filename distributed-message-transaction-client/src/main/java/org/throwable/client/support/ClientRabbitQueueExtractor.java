package org.throwable.client.support;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.throwable.client.configuration.BaseClientProperties;
import org.throwable.common.RabbitConstants;
import org.throwable.utils.AssertUtils;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 17:51
 */
public class ClientRabbitQueueExtractor implements InitializingBean {

    @Autowired
    private ApplicationInfoExtractor applicationInfoExtractor;

    @Autowired
    private BaseClientProperties baseClientProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        AssertUtils.INSTANCE.assertThrowRuntimeException(null != baseClientProperties.getInstanceSign(),
                () -> new IllegalArgumentException("InstanceSign must not be null!"));
    }

    public String getTriggerQueue() {
        return getQueueBound(RabbitConstants.TRIGGER_QUEUE_NAME);
    }

    public String getCheckQueue() {
        return getQueueBound(RabbitConstants.CHECK_QUEUE_NAME);
    }

    private String getQueueBound(String queueName) {
        return String.format("%s.%s.%s", queueName, applicationInfoExtractor.extractApplicationName(), baseClientProperties.getInstanceSign());
    }
}
