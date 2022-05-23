package com.dongzz.quick.common.config;

import com.dongzz.quick.common.config.bean.HttpClientProperties;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Http Client 相关配置
 * 远程 接口 调用
 */
@Configuration
public class HttpClientConfig {

    private final HttpClientProperties httpProperties;

    public HttpClientConfig(HttpClientProperties httpProperties) {
        this.httpProperties = httpProperties;
    }


    /**
     * 连接池管理器
     */
    @Bean(name = "poolingHttpClientManager")
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager poolingManager = new PoolingHttpClientConnectionManager();
        poolingManager.setMaxTotal(httpProperties.getMaxTotal()); // 最大连接数
        poolingManager.setDefaultMaxPerRoute(httpProperties.getDefaultMaxPerRoute()); // 并发数
        return poolingManager;
    }

    /**
     * 连接池
     * 需要 注入 连接池管理器
     */
    @Bean(name = "httpClientBuilder")
    public HttpClientBuilder httpClientBuilder(@Qualifier("poolingHttpClientManager") PoolingHttpClientConnectionManager poolingHttpClientManager) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(poolingHttpClientManager); // 设置 连接池管理器
        return httpClientBuilder;
    }

    /**
     * Http Client 客户端
     * 需要 注入 连接池
     */
    @Bean(name = "httpClient")
    public CloseableHttpClient closeableHttpClient(@Qualifier("httpClientBuilder") HttpClientBuilder httpClientBuilder) {
        return httpClientBuilder.build();
    }

    /**
     * 请求配置 构造器
     * 通过 Builder 设置连接参数
     * 还可以设置 proxy，cookieSpec 等属性
     */
    @Bean(name = "builder")
    public RequestConfig.Builder builder() {
        RequestConfig.Builder builder = RequestConfig.custom();
        builder.setConnectTimeout(httpProperties.getConnectTimeout());
        builder.setConnectionRequestTimeout(httpProperties.getConnectionRequestTimeout());
        builder.setSocketTimeout(httpProperties.getSocketTimeout());
        builder.setStaleConnectionCheckEnabled(httpProperties.isStaleConnectionCheckEnabled());
        return builder;
    }

    /**
     * 请求配置
     * 需要 注入 请求配置构造器
     */
    @Bean(name = "requestConfig")
    public RequestConfig requestConfig(@Qualifier("builder") RequestConfig.Builder builder) {
        return builder.build();
    }

}
