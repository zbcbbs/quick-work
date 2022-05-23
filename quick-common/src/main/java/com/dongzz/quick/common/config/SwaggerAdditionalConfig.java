package com.dongzz.quick.common.config;

import cn.hutool.core.util.IdUtil;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Sets;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.Operation;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * swagger 额外配置
 * 使用 ApiListingScannerPlugin 插件，添加一些额外配置
 */
@Component
public class SwaggerAdditionalConfig implements ApiListingScannerPlugin {

    @Override
    public List<ApiDescription> apply(DocumentationContext context) {
        // 管理员登陆接口
        Operation adminOperation = new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST)
                .summary("管理员登陆")
                .notes("管理员登陆")
                .uniqueId(IdUtil.simpleUUID())
                .consumes(Sets.newHashSet(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) // 请求格式
                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE)) // 响应格式
                .tags(Sets.newHashSet("系统：登陆管理"))
                .parameters(Arrays.asList(
                        new ParameterBuilder()
                                .description("用户名")
                                .type(new TypeResolver().resolve(String.class))
                                .name("username")
                                .defaultValue("admin")
                                .parameterType("query")
                                .parameterAccess("access")
                                .required(true)
                                .modelRef(new ModelRef("string"))
                                .build(),
                        new ParameterBuilder()
                                .description("密码")
                                .type(new TypeResolver().resolve(String.class))
                                .name("password")
                                .defaultValue("123456")
                                .parameterType("query")
                                .parameterAccess("access")
                                .required(true)
                                .modelRef(new ModelRef("string"))
                                .build(),
                        new ParameterBuilder()
                                .description("验证码")
                                .type(new TypeResolver().resolve(String.class))
                                .name("code")
                                .parameterType("query")
                                .parameterAccess("access")
                                .required(false)
                                .modelRef(new ModelRef("string"))
                                .build(),
                        new ParameterBuilder()
                                .description("验证码缓存")
                                .type(new TypeResolver().resolve(String.class))
                                .name("uuid")
                                .parameterType("query")
                                .parameterAccess("access")
                                .required(false)
                                .modelRef(new ModelRef("string"))
                                .build()
                ))
                .responseMessages(Collections.singleton(
                        new ResponseMessageBuilder().code(200).message("OK")
                                .responseModel(new ModelRef("ResponseVo"))
                                .build()))
                .build();
        // 会员登陆接口
        Operation memberOperation = new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST)
                .summary("会员登陆")
                .notes("会员登陆")
                .uniqueId(IdUtil.simpleUUID())
                .consumes(Sets.newHashSet(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) // 请求格式
                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE)) // 响应格式
                .tags(Sets.newHashSet("系统：登陆管理"))
                .parameters(Arrays.asList(
                        new ParameterBuilder()
                                .description("用户名")
                                .type(new TypeResolver().resolve(String.class))
                                .name("username")
                                .defaultValue("zbc")
                                .parameterType("query")
                                .parameterAccess("access")
                                .required(true)
                                .modelRef(new ModelRef("string"))
                                .build(),
                        new ParameterBuilder()
                                .description("密码")
                                .type(new TypeResolver().resolve(String.class))
                                .name("password")
                                .defaultValue("123456")
                                .parameterType("query")
                                .parameterAccess("access")
                                .required(true)
                                .modelRef(new ModelRef("string"))
                                .build(),
                        new ParameterBuilder()
                                .description("验证码")
                                .type(new TypeResolver().resolve(String.class))
                                .name("code")
                                .parameterType("query")
                                .parameterAccess("access")
                                .required(false)
                                .modelRef(new ModelRef("string"))
                                .build(),
                        new ParameterBuilder()
                                .description("验证码缓存")
                                .type(new TypeResolver().resolve(String.class))
                                .name("uuid")
                                .parameterType("query")
                                .parameterAccess("access")
                                .required(false)
                                .modelRef(new ModelRef("string"))
                                .build()
                ))
                .responseMessages(Collections.singleton(
                        new ResponseMessageBuilder().code(200).message("OK")
                                .responseModel(new ModelRef("ResponseVo"))
                                .build()))
                .build();

        // 退出登陆接口
        Operation logoutOperation = new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.GET)
                .summary("退出登陆")
                .notes("退出登陆")
                .uniqueId(IdUtil.simpleUUID())
                .consumes(Sets.newHashSet(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
                .tags(Sets.newHashSet("系统：登陆管理"))
                .parameters(null)
                .responseMessages(Collections.singleton(
                        new ResponseMessageBuilder().code(200).message("OK")
                                .responseModel(new ModelRef("ResponseVo"))
                                .build()))
                .build();
        ApiDescription adminDescription = new ApiDescription("/auth/login", "/auth/login", Arrays.asList(adminOperation), false); // 管理员登陆
        ApiDescription memberDescription = new ApiDescription("/auth/loginVip", "/auth/loginVip", Arrays.asList(memberOperation), false); // 会员登陆
        ApiDescription logoutDescription = new ApiDescription("/auth/logout", "/auth/logout", Arrays.asList(logoutOperation), false); // 退出
        return Arrays.asList(adminDescription, memberDescription, logoutDescription);
    }


    /**
     * 是否启用此插件
     *
     * @param documentationType 文档类型
     * @return
     */
    @Override
    public boolean supports(DocumentationType documentationType) {
        return DocumentationType.SWAGGER_2.equals(documentationType);
    }
}
