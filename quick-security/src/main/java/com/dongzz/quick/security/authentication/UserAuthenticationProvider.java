package com.dongzz.quick.security.authentication;

import com.alibaba.fastjson.JSON;
import com.dongzz.quick.common.exception.VerificationException;
import com.dongzz.quick.common.utils.RedisUtil;
import com.dongzz.quick.security.service.dto.LoginUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

/**
 * 管理员 专用的 认证处理器
 */
public class UserAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    public static final Logger logger = LoggerFactory.getLogger(UserAuthenticationProvider.class);

    private volatile String userNotFoundEncodedPassword;
    private UserDetailsService userDetailsService; // 账号认证
    private PasswordEncoder passwordEncoder; // 加密器
    private RedisUtil redisUtil;

    public UserAuthenticationProvider() {
        this.setPasswordEncoder(new BCryptPasswordEncoder()); // 默认加密
        this.setPreAuthenticationChecks(new DefaultPreAuthenticationChecks());
        this.setPostAuthenticationChecks(new DefaultPostAuthenticationChecks());
    }

    /**
     * 核心认证方法
     * 认证顺序：
     * Ⅰ：验证码认证 verificationCodeAuthenticationChecks() (验证码)
     * Ⅱ：账号认证 UserDetailsService （账号）
     * Ⅲ：前置认证 DefaultPreAuthenticationChecks (账号状态)
     * Ⅳ：核心认证 additionalAuthenticationChecks() (密码)
     * Ⅴ：后置认证 DefaultPostAuthenticationChecks (密码状态)
     *
     * @param authentication 登录信息
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.debug("User login，username：{}", authentication.getName());
        verificationCodeAuthenticationChecks((UsernamePasswordAuthenticationToken) authentication);
        return super.authenticate(authentication);
    }

    /**
     * 验证码认证 Ⅰ
     *
     * @param authentication 登录信息
     */
    protected void verificationCodeAuthenticationChecks(UsernamePasswordAuthenticationToken authentication) {
        DefaultAuthenticationDetails details = (DefaultAuthenticationDetails) authentication.getDetails();
        String code = details.getCode(); // 输入的
        String uuid = details.getUuid(); // 缓存标记
        if (StringUtils.isNotBlank(code)) {
            String cacheCode = (String) redisUtil.get(uuid); // 真实的
            redisUtil.del(uuid); // 清除缓存
            logger.debug("Verification code，input: {} == cache: {}", code, cacheCode);
            if (StringUtils.isBlank(cacheCode)) {
                throw new VerificationException(HttpStatus.UNAUTHORIZED, "验证码不存在或已过期");
            }
            if (!cacheCode.equalsIgnoreCase(code)) {
                throw new VerificationException(HttpStatus.UNAUTHORIZED, "验证码不正确");
            }
        }
    }

    /**
     * 账号认证 Ⅱ
     *
     * @param username       账号
     * @param authentication 登录信息
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        this.prepareTimingAttackProtection();

        try {
            LoginUser loadedUser = (LoginUser) this.getUserDetailsService().loadUserByUsername(username);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
            } else {
                // 可以对 UserDetails 执行某些附加操作
                logger.debug("User details service: {}", JSON.toJSONString(loadedUser));
                return loadedUser;
            }
        } catch (UsernameNotFoundException var4) {
            this.mitigateAgainstTimingAttack(authentication);
            throw var4;
        } catch (InternalAuthenticationServiceException var5) {
            throw var5;
        } catch (Exception var6) {
            throw new InternalAuthenticationServiceException(var6.getMessage(), var6);
        }
    }

    /**
     * 核心认证 Ⅳ
     *
     * @param userDetails    账号信息
     * @param authentication 登录信息
     * @throws AuthenticationException
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() != null && !"".equals(authentication.getCredentials().toString())) {
            logger.debug("Credentials: {}", authentication.getCredentials().toString());
            // 密码验证
            String presentedPassword = authentication.getCredentials().toString(); // 前台输入
            if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
                throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
            }
        }
    }

    /**
     * 前置认证 Ⅲ
     */
    private class DefaultPreAuthenticationChecks implements UserDetailsChecker {

        private DefaultPreAuthenticationChecks() {

        }

        /**
         * 账号状态认证
         *
         * @param user 账号信息
         */
        public void check(UserDetails user) {
            logger.debug("Username status: locked={}, enabled={}, expired={}", !user.isAccountNonLocked(), user.isEnabled(), !user.isAccountNonExpired());
            if (!user.isAccountNonLocked()) {
                throw new LockedException("账号已锁定");
            } else if (!user.isEnabled()) {
                throw new DisabledException("账号已禁用");
            } else if (!user.isAccountNonExpired()) {
                throw new AccountExpiredException("账号已过期");
            }
        }
    }

    /**
     * 后置认证 Ⅴ
     */
    private class DefaultPostAuthenticationChecks implements UserDetailsChecker {

        private DefaultPostAuthenticationChecks() {

        }

        /**
         * 密码状态认证
         *
         * @param user 账号信息
         */
        public void check(UserDetails user) {
            logger.debug("Credentials status: expired={}", !user.isCredentialsNonExpired());
            if (!user.isCredentialsNonExpired()) {
                throw new CredentialsExpiredException("密码已过期");
            }
        }
    }

    /**
     * ProviderManager 会遍历所有 SecurityConfig 中注册的 Provider 集合
     * 根据此方法返回 true 或 false 来决定使用哪个 Provider
     * 校验前台请求过来的 Authentication 当前仅处理 UserAuthenticationToken
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UserAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    protected void doAfterPropertiesSet() throws Exception {
        Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
    }

    private void prepareTimingAttackProtection() {
        if (this.userNotFoundEncodedPassword == null) {
            this.userNotFoundEncodedPassword = this.passwordEncoder.encode("userNotFoundPassword");
        }

    }

    private void mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken authentication) {
        if (authentication.getCredentials() != null) {
            String presentedPassword = authentication.getCredentials().toString();
            this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
        }

    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userNotFoundEncodedPassword = null;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    protected UserDetailsService getUserDetailsService() {
        return this.userDetailsService;
    }

    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }
}
