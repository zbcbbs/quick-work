package com.dongzz.quick.common.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 集成 Knife4j 在线api工具 核心配置
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
public class SwaggerConfig {

    @Value("${jwt.header}")
    private String tokenHeader; // 令牌请求头

    //------------------------------- 接口分组 ------------------------------

    /**
     * 全部接口
     */
    @Bean
    public Docket docketDefaultApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(defaultApiInfo()) // api 说明信息
                .securitySchemes(securitySchemes()) // 全局权限设置
                .securityContexts(securityContexts())
                .useDefaultResponseMessages(false) // 禁用默认响应信息
//                .globalOperationParameters(globalOperationParameters()) // 全局参数
                .groupName("全部接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.dongzz.quick")) // 生成 api 包路径
                .paths(PathSelectors.any())
                .build();
    }


    /**
     * 系统接口
     */
    @Bean
    public Docket docketSysApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(sysApiInfo())
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                .useDefaultResponseMessages(false)
                .groupName("系统接口")
                .pathMapping("/")
                .select()
                .apis(Predicates.or(
                        RequestHandlerSelectors.basePackage("com.dongzz.quick.security"),
                        RequestHandlerSelectors.basePackage("com.dongzz.quick.generator"),
                        RequestHandlerSelectors.basePackage("com.dongzz.quick.logging"),
                        RequestHandlerSelectors.basePackage("com.dongzz.quick.tools"),
                        RequestHandlerSelectors.basePackage("com.dongzz.quick.modules.system"),
                        RequestHandlerSelectors.basePackage("com.dongzz.quick.modules.quartz"),
                        RequestHandlerSelectors.basePackage("com.dongzz.quick.modules.mnt")
                ))
                .paths(PathSelectors.any())
                .build();
    }

    //----------------------------------- 接口描述 ----------------------------------

    /**
     * 全部接口说明
     */
    public ApiInfo defaultApiInfo() {
        return new ApiInfoBuilder()
                .title("快速开发平台接口文档")
                .description("基于Spring Boot 2.x的快速开发平台")
                .termsOfServiceUrl("http://quick.zbcbbs.com") // 项目地址
                .license("Apache License v2.0")
                .contact(new Contact("北辰", "http://zbcbbs.com", "zbcbbs@163.com"))
                .version("1.0.1").build();
    }

    /**
     * 系统接口说明
     */
    public ApiInfo sysApiInfo() {
        return new ApiInfoBuilder()
                .title("系统接口")
                .description("系统相关的接口")
                .termsOfServiceUrl("http://quick.zbcbbs.com")
                .license("Apache License v2.0")
                .contact(new Contact("北辰", "http://zbcbbs.com", "zbcbbs@163.com"))
                .version("1.0.1").build();
    }

    //------------------------------- 全局参数配置 ---------------------------------

    /**
     * 全局参数
     * 每个请求都会携带
     */
    private List<Parameter> globalOperationParameters() {
        // 全局参数集合
        List<Parameter> parameters = new ArrayList<>();
        // 添加
        parameters.add(new ParameterBuilder()
                .name("Var")
                .description("参数")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(true)
                .allowableValues(new AllowableListValues(Arrays.asList("a", "b"), "string")) // 下拉框
                .defaultValue("a") // 默认
                .build());

        return parameters;
    }


    //--------------------------------- 全局认证配置，JWT令牌 ---------------------------------

    private List<ApiKey> securitySchemes() {
        List<ApiKey> list = new ArrayList<>();
        list.add(new ApiKey(tokenHeader, tokenHeader, "header")); // 请求头 输入 JWT令牌
        return list;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> list = new ArrayList<>();
        list.add(SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build());
        return list;
    }

    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> list = new ArrayList<>();
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything"); // 授权范围
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        list.add(new SecurityReference(tokenHeader, authorizationScopes));
        return list;
    }
}
