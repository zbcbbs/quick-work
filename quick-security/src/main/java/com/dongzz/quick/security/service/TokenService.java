package com.dongzz.quick.security.service;

import com.dongzz.quick.security.service.dto.LoginUser;
import com.dongzz.quick.security.service.dto.OnlineUser;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * 令牌相关服务接口
 * 1.令牌信息可存储到 redis 或 数据库
 * 2.默认存储到redis，实现类为 TokenServiceJWTImpl
 * 若要存储到数据库，则在实现类 TokenServiceDbImpl 上添加注解 @Primary，即优先使用此实现类
 */
public interface TokenService {

    // 缓存
    String LOGIN_USER_KEY = "login_user"; // uuid
    String LOGIN_USER_ROLE = "is_admin"; // 管理员


    /**
     * 生成令牌
     *
     * @param loginUser 认证信息
     * @return
     */
    String createJWTToken(LoginUser loginUser);

    /**
     * 获取 请求中令牌
     *
     * @param request 请求
     * @return
     */
    String getJWTToken(HttpServletRequest request);

    /**
     * 令牌刷新 续期
     *
     * @param token 令牌
     */
    void refresh(String token);

    /**
     * 解析令牌 获取 缓存 UUID
     *
     * @param token 令牌
     * @return
     */
    String getUUIDFromJWT(String token);

    /**
     * 解析令牌 获取 Claims
     *
     * @param token 令牌
     * @return
     */
    Claims getClaims(String token);


    /**
     * 根据在线用户 构建认证信息
     *
     * @param onlineUser 在线用户
     * @return
     */
    Authentication getAuthentication(OnlineUser onlineUser);
}
