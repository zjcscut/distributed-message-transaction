package org.throwable.server.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/2 16:30
 */
@Data
@ConfigurationProperties(prefix = "dmt.server.druid")
public class DruidProperties {

    private Boolean enable = Boolean.TRUE;
    private String url;
    private String driverClassName;
    private String username;
    private String password;
    private Properties properties;
}
