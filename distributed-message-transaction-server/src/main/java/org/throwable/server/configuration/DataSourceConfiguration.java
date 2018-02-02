package org.throwable.server.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.Assert;


/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/2 15:54
 */
@EnableConfigurationProperties(value = DruidProperties.class)
@ConditionalOnProperty(value = "dmt.server.druid.enable", matchIfMissing = true)
@Configuration
public class DataSourceConfiguration {

    private final DruidProperties druidProperties;

    public DataSourceConfiguration(DruidProperties druidProperties) {
        this.druidProperties = druidProperties;
    }

    @Bean
    public DruidDataSource druidDataSource() throws Exception {
        Assert.notNull(druidProperties.getUrl(), "Url must not be null!");
        Assert.notNull(druidProperties.getDriverClassName(), "DriverClassName must not be null!");
        Assert.notNull(druidProperties.getUsername(), "Username must not be null!");
        Assert.notNull(druidProperties.getPassword(), "Password must not be null!");
        DruidDataSource dataSource;
        if (null != druidProperties.getProperties()) {
            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(druidProperties.getProperties());
        } else {
            dataSource = new DruidDataSource();
        }
        dataSource.setUrl(druidProperties.getUrl());
        dataSource.setDriverClassName(druidProperties.getDriverClassName());
        dataSource.setUsername(druidProperties.getUsername());
        dataSource.setPassword(druidProperties.getPassword());
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(DruidDataSource druidDataSource) {
        return new DataSourceTransactionManager(druidDataSource);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DruidDataSource druidDataSource) {
        return new JdbcTemplate(druidDataSource);
    }
}
