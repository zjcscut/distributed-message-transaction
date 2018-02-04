package org.throwable.server.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 16:02
 */
@Data
@ConfigurationProperties(prefix = "dmt.server.base")
public class BaseProperties {

    private static final Long DEFAULT_CONFIRM_INTERVAL_MILLISECOND = 5000L;

    /**
     * 确认间隔，默认为5000毫秒
     */
    private Long confirmIntervalMillisecond = DEFAULT_CONFIRM_INTERVAL_MILLISECOND;
}
