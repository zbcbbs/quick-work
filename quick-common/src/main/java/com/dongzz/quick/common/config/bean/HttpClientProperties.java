package com.dongzz.quick.common.config.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * http client 配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "http.client")
public class HttpClientProperties {

    private Integer maxTotal; // 最大连接数
    private Integer defaultMaxPerRoute; // 并发数
    private Integer connectTimeout; // 创建连接最长时间
    private Integer connectionRequestTimeout; // 连接池中获取到连接的最长时间
    private Integer socketTimeout; // 数据传输的最长时间
    private boolean staleConnectionCheckEnabled; // 提交请求前测试连接是否可用

}
