package com.dongzz.quick.common.config.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Druid 数据源配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.druid")
public class DruidProperties {

    private String url;
    private String driverClassName;
    private String username;
    private String password;
    private int initialSize;
    private int minIdle;
    private int maxActive;
    private long maxWait;
    private long timeBetweenEvictionRunsMillis;
    private long minEvictableIdleTimeMillis;
    private String validationQuery;
    private boolean testWhileIdle;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private boolean poolPreparedStatements;
    private int maxPoolPreparedStatementPerConnectionSize;
    private boolean useGlobalDataSourceStat;
    private String filters; // 扩展插件
    // 监控慢SQL
    @Value("${spring.datasource.druid.filter.stat.log-slow-sql}")
    private boolean logSlowSql;
    @Value("${spring.datasource.druid.filter.stat.slow-sql-millis}")
    private long slowSqlMillis;

}
