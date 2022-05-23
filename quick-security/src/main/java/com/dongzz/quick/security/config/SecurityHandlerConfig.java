package com.dongzz.quick.security.config;

import cn.hutool.core.util.IdUtil;
import com.dongzz.quick.common.utils.WebUtil;
import com.dongzz.quick.common.domain.ResponseVo;
import com.dongzz.quick.security.config.bean.JwtProperties;
import com.dongzz.quick.security.config.bean.LoginProperties;
import com.dongzz.quick.security.service.OnlineUserService;
import com.dongzz.quick.security.service.TokenService;
import com.dongzz.quick.security.service.dto.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * spring security 处理器配置
 */
@Configuration
public class SecurityHandlerConfig {

    public static final Logger logger = LoggerFactory.getLogger(SecurityHandlerConfig.class);

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private LoginProperties loginProperties;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private OnlineUserService onlineUserService;

    /**
     * 登陆成功 处理器
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // 获取认证信息
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();

            // 日志
            logger.debug("User login success: Username={}", loginUser.getUsername());

            // 登陆成功
            loginUser.setUuid(IdUtil.randomUUID()); // 缓存标记
            String token = tokenService.createJWTToken(loginUser); // 令牌生成
            onlineUserService.save(loginUser, token, request); // 缓存

            // 响应
            Map<String, Object> result = new HashMap<>();
            result.put("token", jwtProperties.getTokenStartWith() + token);
            result.put("user", loginUser);

            // 限制单用户登录 剔除已登录的用户
            if (loginProperties.isSingleLogin()) {
                onlineUserService.checkLoginOnUser(loginUser.getUsername(), token);
            }

            WebUtil.flushOutJson(response, HttpStatus.OK.value(), new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), result));
        };
    }

    /**
     * 登陆失败 处理器
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            String msg = null;
            if (exception instanceof BadCredentialsException) {
                msg = "密码错误";
            } else {
                msg = exception.getMessage();
            }
            // 登陆失败 响应401 提示错误信息
            ResponseVo info = new ResponseVo(HttpStatus.UNAUTHORIZED.value(), msg);
            WebUtil.flushOutJson(response, HttpStatus.UNAUTHORIZED.value(), info);
        };
    }

    /**
     * 认证异常 处理器
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {

        return (request, response, authException) -> {
            // 统一响应 401 需要先登陆
            ResponseVo info = new ResponseVo(HttpStatus.UNAUTHORIZED.value(), "请先登录");
            WebUtil.flushOutJson(response, HttpStatus.UNAUTHORIZED.value(), info);
        };
    }

    /**
     * 退出登陆成功 处理器
     */
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            ResponseVo info = new ResponseVo(HttpStatus.OK.value(), "退出成功");
            // 拦截令牌
            String token = tokenService.getJWTToken(request);
            // 删除缓存
            onlineUserService.logout(token);
            WebUtil.flushOutJson(response, HttpStatus.OK.value(), info);
        };

    }
}
