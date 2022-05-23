package com.dongzz.quick.security.config.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 安全 参数配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "secure")
public class SecurityProperties {

    private List<String> ignoredUrls;

    private boolean rsaEnable; // 密码加密传输
    private String privateKey; // 私钥
}
