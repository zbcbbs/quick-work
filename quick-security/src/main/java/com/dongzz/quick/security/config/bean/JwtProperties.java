package com.dongzz.quick.security.config.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Jwt 令牌参数配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * 请求头标记： Authorization
     */
    private String header;

    /**
     * 令牌前缀，最后留个空格 Bearer
     */
    private String tokenStartWith;

    /**
     * 用最少88位的Base64对该令牌进行编码
     */
    private String base64Secret;

    /**
     * 令牌过期时间 单位/毫秒
     */
    private Long tokenExpireTime;

    /**
     * 移动端 令牌过期时间 单位/毫秒
     */
    private Long tokenAppExpireTime;

    /**
     * 在线用户 key，根据 key 查询 redis 中在线用户的数据
     */
    private String onlineKey;

    /**
     * 验证码 key
     */
    private String codeKey;

    /**
     * 令牌续期检查时间范围
     */
    private Long detect;

    /**
     * 续期时间
     */
    private Long renew;

    /**
     * 获取令牌前缀 添加空格
     */
    public String getTokenStartWith() {
        return tokenStartWith + " ";
    }
}
