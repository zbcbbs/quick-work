package com.dongzz.quick.security.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 前台提交的 会员认证信息
 */
public class MemberAuthenticationToken extends UsernamePasswordAuthenticationToken {


    public MemberAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public MemberAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
