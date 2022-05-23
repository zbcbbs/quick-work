package com.dongzz.quick.security.filter;

import cn.hutool.core.collection.CollectionUtil;
import com.dongzz.quick.security.config.bean.JwtProperties;
import com.dongzz.quick.security.config.bean.SecurityProperties;
import com.dongzz.quick.security.service.OnlineUserService;
import com.dongzz.quick.security.service.TokenService;
import com.dongzz.quick.security.service.UserCacheClean;
import com.dongzz.quick.security.service.dto.OnlineUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 身份令牌 JWT 过滤器
 */
@Component
public class SecurityTokenFilter extends GenericFilterBean {

    public static final Logger logger = LoggerFactory.getLogger(SecurityTokenFilter.class);

    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private OnlineUserService onlineUserService;
    @Autowired
    private UserCacheClean userCacheClean; // 用户缓存清理

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 解决 axios 发送 '预检' 请求 导致出现两次请求的情况
        if ("OPTIONS".equals(request.getMethod())) {
            // 预检请求 直接放行
            response.setStatus(HttpStatus.OK.value());
        }

        // 白名单放行
        boolean isPass = false;
        List<String> ignoredUrls = securityProperties.getIgnoredUrls();
        String requestURI = request.getRequestURI();
        PathMatcher pathMatcher = new AntPathMatcher();
        if (CollectionUtil.isNotEmpty(ignoredUrls)) {
            for (String url : ignoredUrls) {
                if (pathMatcher.match(url, requestURI)) {
                    isPass = true;
                    break;
                }
            }
        }

        if (!isPass) {
            // 获取请求中的令牌
            String token = tokenService.getJWTToken(request);
            if (StringUtils.isNotBlank(token)) {
                logger.debug("JWT filter: online-token={}", jwtProperties.getOnlineKey() + token);
                OnlineUser onlineUser = null;
                boolean cleanCache = false; // 清理缓存
                try {
                    onlineUser = onlineUserService.getOne(jwtProperties.getOnlineKey() + token);
                } catch (ExpiredJwtException e) {
                    logger.error(e.getMessage());
                    cleanCache = true;
                } finally {
                    if (cleanCache || Objects.isNull(onlineUser)) {
                        // 判断角色
                        Claims claims = tokenService.getClaims(token);
                        Boolean isAdmin = claims.get(TokenService.LOGIN_USER_ROLE, Boolean.class);
                        if (isAdmin) {
                            userCacheClean.cleanUserCache(claims.getSubject());
                        } else {
                            userCacheClean.cleanMemCache(claims.getSubject());
                        }
                    }
                }
                if (onlineUser != null && org.springframework.util.StringUtils.hasText(token)) {
                    // 刷新上下文中的认证信息
                    // 若令牌过期失效，则根据令牌无法找到缓存的用户信息 此处不会执行，导致身份信息在上下文中丢失，响应 401 未登陆
                    Authentication authentication = tokenService.getAuthentication(onlineUser);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    tokenService.refresh(token); // 令牌续期
                }
            }

        }

        filterChain.doFilter(request, response);
    }
}
