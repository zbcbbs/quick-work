package com.dongzz.quick.security.service.impl;

import com.dongzz.quick.security.service.TokenService;
import com.dongzz.quick.security.service.dto.LoginUser;
import com.dongzz.quick.security.service.dto.OnlineUser;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;

/**
 * 账号信息 缓存到 关系型数据库
 */
//@Primary
@Service
public class TokenServiceDbImpl implements TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenServiceDbImpl.class);

    // 签名密钥
    private Key KEY = null;

    @Override
    public String createJWTToken(LoginUser loginUser) {
        return null;
    }

    @Override
    public String getJWTToken(HttpServletRequest request) {
        return null;
    }

    @Override
    public void refresh(String token) {

    }

    @Override
    public String getUUIDFromJWT(String token) {
        return null;
    }

    @Override
    public Claims getClaims(String token) {
        return null;
    }

    @Override
    public Authentication getAuthentication(OnlineUser onlineUser) {
        return null;
    }
}
