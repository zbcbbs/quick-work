package com.dongzz.quick.security.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 前台提交的 管理员认证信息
 */
public class UserAuthenticationToken extends UsernamePasswordAuthenticationToken {


    public UserAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public UserAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
