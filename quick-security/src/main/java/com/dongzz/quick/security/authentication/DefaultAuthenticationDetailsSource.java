package com.dongzz.quick.security.authentication;

import org.springframework.security.authentication.AuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

/**
 * 前台提交的 额外认证信息
 */
public class DefaultAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, DefaultAuthenticationDetails> {

    public DefaultAuthenticationDetailsSource() {

    }

    @Override
    public DefaultAuthenticationDetails buildDetails(HttpServletRequest request) {
        return new DefaultAuthenticationDetails(request);
    }
}
