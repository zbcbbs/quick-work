package com.dongzz.quick.security.filter;

import com.dongzz.quick.common.utils.RsaUtil;
import com.dongzz.quick.security.authentication.UserAuthenticationToken;
import com.dongzz.quick.security.config.bean.SecurityProperties;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 管理员 认证过滤器
 */
@Setter
public class UserAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final Logger logger = LoggerFactory.getLogger(UserAuthenticationFilter.class);

    private String usernameParameter = "username";
    private String passwordParameter = "password";
    private boolean postOnly = true;

    private SecurityProperties securityProperties;

    public UserAuthenticationFilter() {
        super(new AntPathRequestMatcher("/auth/login", "POST")); // 请求路径
    }

    /**
     * 封装 前台提交的 认证信息
     *
     * @param request  请求
     * @param response 响应
     * @return
     * @throws AuthenticationException
     */
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            // 账密
            String username = this.obtainUsername(request);
            String password = this.obtainPassword(request);
            if (username == null) {
                username = "";
            }
            if (password == null) {
                password = "";
            }
            username = username.trim();

            // 若密码加密传输，则需要私钥解密
            if (securityProperties.isRsaEnable()) {
                try {
                    password = RsaUtil.decryptByPrivateKey(securityProperties.getPrivateKey(), password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 封装认证信息
            UserAuthenticationToken authRequest = new UserAuthenticationToken(username, password);
            this.setDetails(request, authRequest); // 额外信息
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(this.passwordParameter);
    }

    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(this.usernameParameter);
    }

    protected void setDetails(HttpServletRequest request, AbstractAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

}
