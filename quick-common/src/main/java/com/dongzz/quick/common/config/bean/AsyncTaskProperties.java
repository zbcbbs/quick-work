package com.dongzz.quick.common.config.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 异步线程池配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "task.pool")
public class AsyncTaskProperties {

    private Integer corePoolSize;
    private Integer maxPoolSize;
    private Integer queueCapacity;
    private Integer keepAliveSeconds;
    private String threadNamePrefix;

}
