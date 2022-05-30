package com.dongzz.quick.security.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.dongzz.quick.common.utils.RedisUtil;
import com.dongzz.quick.security.authentication.MemberAuthenticationToken;
import com.dongzz.quick.security.authentication.UserAuthenticationToken;
import com.dongzz.quick.security.config.bean.JwtProperties;
import com.dongzz.quick.security.service.TokenService;
import com.dongzz.quick.security.service.dto.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 账号信息 缓存到  redis 的实现
 * JWT框架实现 token 操作
 */
@Primary
@Service
public class TokenServiceJWTImpl implements TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenServiceJWTImpl.class);

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private RedisUtil redisUtil;
    // Jwt 签名密钥
    private Key KEY = null;

    /**
     * 创建 jwt token
     *
     * @param loginUser 在线用户
     * @return
     */
    @Override
    public String createJWTToken(LoginUser loginUser) {
        Map<String, Object> claims = new HashMap<>();
        // 缓存
        claims.put(LOGIN_USER_KEY, loginUser.getUuid()); // uuid
        claims.put(LOGIN_USER_ROLE, loginUser.isAdmin()); // 管理员
        // 创建JWT （header + payload + signature） 头部 + 数据存储 + 签名密钥
        String jwtToken = Jwts.builder().setId(loginUser.getUuid()).setClaims(claims).setSubject(loginUser.getUsername()).signWith(SignatureAlgorithm.HS256, getKeyInstance())
                .compact();
        return jwtToken;
    }

    @Override
    public String getJWTToken(HttpServletRequest request) {
        final String requestHeader = request.getHeader(jwtProperties.getHeader());
        if (requestHeader != null && requestHeader.startsWith(jwtProperties.getTokenStartWith())) {
            return requestHeader.substring(7);
        }
        return null;
    }

    @Override
    public void refresh(String token) {
        // 判断是否续期token，计算过期时间
        long time = redisUtil.getExpire(jwtProperties.getOnlineKey() + token) * 1000;
        Date expireDate = DateUtil.offset(new Date(), DateField.MILLISECOND, (int) time);
        // 判断当前时间与过期时间的时间差
        long differ = expireDate.getTime() - System.currentTimeMillis();
        // 若在续期检查的范围内，则续期
        if (differ <= jwtProperties.getDetect()) {
            long renew = time + jwtProperties.getRenew();
            redisUtil.expire(jwtProperties.getOnlineKey() + token, renew, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 令牌字符串中 取出 账号缓存标识
     *
     * @param token 令牌
     * @return
     */
    @Override
    public String getUUIDFromJWT(String token) {
        if ("null".equals(token) || StringUtils.isBlank(token)) {
            return null;
        }
        try {
            // 根据签名密钥 获取 claims 数据存储
            Map<String, Object> jwtClaims = Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(token)
                    .getBody();
            return MapUtils.getString(jwtClaims, LOGIN_USER_KEY);
        } catch (ExpiredJwtException e) {
            logger.error("{}已过期", token);
        } catch (Exception e) {
            logger.error("获取uuid异常", e);
        }
        return null;
    }

    /**
     * 解析令牌 获取 Claims
     *
     * @param token 令牌
     * @return
     */
    @Override
    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(getKeyInstance()).parseClaimsJws(token)
                .getBody();
    }

    @Override
    public Authentication getAuthentication(LoginUser loginUser) {
        // 判断是否管理员
        boolean isAdmin = loginUser.isAdmin();
        if (!isAdmin) {
            return new MemberAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        }
        // 重新构造认证信息
        return new UserAuthenticationToken(loginUser, null, loginUser.getAuthorities());
    }

    /**
     * 生成 jwt 签名密钥
     *
     * @return
     */
    private Key getKeyInstance() {
        if (KEY == null) {
            synchronized (TokenServiceJWTImpl.class) {
                if (KEY == null) { // 双重锁
                    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtProperties.getBase64Secret());
                    KEY = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
                }
            }
        }
        return KEY;
    }

}
