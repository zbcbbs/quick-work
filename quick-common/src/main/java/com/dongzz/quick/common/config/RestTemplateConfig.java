package com.dongzz.quick.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * RestTemplate 模板类 相关配置
 * 远程 接口 调用
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Http 请求 客户端工厂
     */
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(30000); // 读取数据超时时间 ms
        factory.setConnectTimeout(30000); // 连接超时时间 ms
        return factory;
    }

    /**
     * RestTemplate
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        // Http 消息转化器
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.stream().forEach(messageConverter -> {
            if (messageConverter instanceof StringHttpMessageConverter) {
                StringHttpMessageConverter stringHttpMessageConverter = (StringHttpMessageConverter) messageConverter;
                stringHttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8); // 处理中文乱码
            }
        });
        return restTemplate;
    }
}
