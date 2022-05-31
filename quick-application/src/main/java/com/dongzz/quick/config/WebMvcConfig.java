package com.dongzz.quick.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.dongzz.quick.common.config.bean.FileProperties;
import com.dongzz.quick.common.plugin.datatables.PageTableArgumentResolver;
import com.dongzz.quick.common.plugin.vuetables.VueTableArgumentResolver;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * spring mvc 核心配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final FileProperties fileProperties;

    public WebMvcConfig(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }


    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 配置
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); // 域名
        configuration.addAllowedHeader("*"); // 请求头
        configuration.addAllowedMethod("*"); // 请求方式
        configuration.setAllowCredentials(true); // 可以携带 Cookie
        configuration.setMaxAge(1800L);
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }

    /**
     * jQuery datatable 分页参数解析器
     */
    @Bean
    public PageTableArgumentResolver pageTableArgumentResolver() {
        return new PageTableArgumentResolver();
    }

    /**
     * Vue table 分页参数解析器
     */
    @Bean
    public VueTableArgumentResolver vueTableArgumentResolver() {
        return new VueTableArgumentResolver();
    }

    /**
     * 注册参数解析器
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(pageTableArgumentResolver());
        argumentResolvers.add(vueTableArgumentResolver());
    }

    /**
     * 静态资源访问映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        FileProperties.ElPath path = fileProperties.getPath();
        FileProperties.ElMapping mapping = fileProperties.getMapping();
        // 磁盘地址
        String avatarUrl = "file:" + path.getAvatar();
        String pathUrl = "file:" + path.getPath();

        registry.addResourceHandler(mapping.getAvatar() + "/**").addResourceLocations(avatarUrl).setCachePeriod(0);
        registry.addResourceHandler(mapping.getPath() + "/**").addResourceLocations(pathUrl).setCachePeriod(0);
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/").setCachePeriod(0); // css，js，image
    }

    /**
     * 消息转换器
     */
    @Bean
    public HttpMessageConverters httpMessageConverters() {
        // 集成fastjson，替换jackson
        FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        // 配置
        FastJsonConfig config = new FastJsonConfig();
        config.setDateFormat("yyyy-MM-dd HH:mm:ss"); // 日期格式化
        config.setSerializerFeatures(SerializerFeature.PrettyFormat); // 序列化特性
        fastJsonConverter.setFastJsonConfig(config);
        fastJsonConverter.setSupportedMediaTypes(supportedMediaTypes);
        fastJsonConverter.setDefaultCharset(StandardCharsets.UTF_8);
        return new HttpMessageConverters(fastJsonConverter);
    }
}
