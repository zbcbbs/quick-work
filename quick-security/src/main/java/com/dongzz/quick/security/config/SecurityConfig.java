package com.dongzz.quick.security.config;

import cn.hutool.core.util.ArrayUtil;
import com.dongzz.quick.common.annotation.AnonymousAccess;
import com.dongzz.quick.common.enums.RequestMethodEnum;
import com.dongzz.quick.common.utils.RedisUtil;
import com.dongzz.quick.security.authentication.DefaultAuthenticationDetailsSource;
import com.dongzz.quick.security.authentication.MemberAuthenticationProvider;
import com.dongzz.quick.security.authentication.UserAuthenticationProvider;
import com.dongzz.quick.security.config.bean.SecurityProperties;
import com.dongzz.quick.security.filter.MemberAuthenticationFilter;
import com.dongzz.quick.security.filter.SecurityTokenFilter;
import com.dongzz.quick.security.filter.UserAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

/**
 * spring security 权限配置
 * prePostEnabled :决定 Spring Security 的前置注解是否可用 [@PreAuthorize,@PostAuthorize,..]
 * secureEnabled : 决定是否 Spring Security 的保障注解 [@Secured] 是否可用.
 * jsr250Enabled ：决定 JSR-250 annotations 注解[@RolesAllowed..] 是否可用.
 */

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 多种登录方式 认证处理
    @Autowired
    @Qualifier("UserDetailsService")
    private UserDetailsService userDetailsService; // 管理员认证
    @Autowired
    @Qualifier("MemberDetailsService")
    private UserDetailsService memberDetailsService; // 会员认证
    @Autowired
    private SecurityTokenFilter securityTokenFilter; // 令牌过滤器
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SecurityProperties securityProperties;


    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler; // 认证成功处理器
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler; // 认证失败处理器
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;   // 认证异常处理器
    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler; // 退出成功处理器

    /**
     * 注册 多个 身份认证器
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(userAuthenticationProvider()); // 管理员专用
        auth.authenticationProvider(memberAuthenticationProvider()); // 会员专用
    }

    /**
     * 核心 配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 读取配置中的白名单路径
        String[] ignoredUrls = ArrayUtil.toArray(securityProperties.getIgnoredUrls(), String.class);

        // 通过处理器映射器 获取匿名访问路径
        RequestMappingHandlerMapping handlerMapping = (RequestMappingHandlerMapping) applicationContext.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = handlerMapping.getHandlerMethods(); // 获取所有控制器方法
        Map<String, Set<String>> anonymousUrls = getAnonymousUrl(handlerMethodMap); // 筛选所有匿名路径

        // 启用跨域支持 防止csrf攻击
        http.cors().and().csrf().disable();
        // 禁用 session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 无状态登录

        // 设置过滤器
        http.addFilterBefore(securityTokenFilter, AbstractPreAuthenticatedProcessingFilter.class); // 令牌过滤器
        http.addFilterBefore(userAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class); // 管理员认证
        http.addFilterBefore(memberAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class); // 会员认证

        // 访问控制
        http.authorizeRequests()
                // 跨域请求中的 Preflight 请求放行
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                // 路径放行
                .antMatchers(
                        "/",
                        "/doc.html",
                        "/favicon.ico",
                        "/v2/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/druid/**",
                        "/static/**",
                        "/webSocket/**",
                        "/auth/login",
                        "/auth/loginVip",
                        "/auth/code"
                ).permitAll()
                .antMatchers(ignoredUrls).permitAll() // 白名单放行
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // 放行OPTIONS请求
                // 带有匿名标记的接口，可以直接访问
                // GET
                .antMatchers(HttpMethod.GET, anonymousUrls.get(RequestMethodEnum.GET.getType()).toArray(new String[0])).permitAll()
                // POST
                .antMatchers(HttpMethod.POST, anonymousUrls.get(RequestMethodEnum.POST.getType()).toArray(new String[0])).permitAll()
                // PUT
                .antMatchers(HttpMethod.PUT, anonymousUrls.get(RequestMethodEnum.PUT.getType()).toArray(new String[0])).permitAll()
                // PATCH
                .antMatchers(HttpMethod.PATCH, anonymousUrls.get(RequestMethodEnum.PATCH.getType()).toArray(new String[0])).permitAll()
                // DELETE
                .antMatchers(HttpMethod.DELETE, anonymousUrls.get(RequestMethodEnum.DELETE.getType()).toArray(new String[0])).permitAll()
                // 所有类型的接口都放行
                .antMatchers(anonymousUrls.get(RequestMethodEnum.ALL.getType()).toArray(new String[0])).permitAll()
                .anyRequest().authenticated(); // 任何无法匹配上的URL，需要用户身份认证后才能访问

        // 表单登陆配置
        FormLoginConfigurer<HttpSecurity> formLogin = http.formLogin();
        formLogin.and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint); // 认证异常处理器

        // 退出登陆配置
        LogoutConfigurer<HttpSecurity> logout = http.logout();
        logout.logoutUrl("/auth/logout").logoutSuccessHandler(logoutSuccessHandler); // 退出登陆处理器

        // 解决不允许显示在iframe的问题
        http.headers().frameOptions().disable();
        http.headers().cacheControl();

    }

    /**
     * 密码加密器
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 认证管理器
     */
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //------------------------- 认证处理器 -----------------------------------

    /**
     * 管理员 认证处理器
     */
    @Bean
    public UserAuthenticationProvider userAuthenticationProvider() {
        UserAuthenticationProvider provider = new UserAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService); // 账号认证
        provider.setRedisUtil(redisUtil);
        provider.setPasswordEncoder(bCryptPasswordEncoder()); // 加密器
        provider.setHideUserNotFoundExceptions(false); // 隐藏 账号不存在异常（统一显示为密码错误异常）
        return provider;
    }

    /**
     * 会员 认证处理器
     */
    @Bean
    public MemberAuthenticationProvider memberAuthenticationProvider() {
        MemberAuthenticationProvider provider = new MemberAuthenticationProvider();
        provider.setUserDetailsService(memberDetailsService);
        provider.setRedisUtil(redisUtil);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    //---------------------- 认证过滤器 ------------------------------------

    /**
     * 管理员 认证过滤器
     */
    @Bean
    public UserAuthenticationFilter userAuthenticationFilter() throws Exception {
        UserAuthenticationFilter filter = new UserAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean()); // 认证管理器
        filter.setAuthenticationDetailsSource(defaultAuthenticationDetailsSource()); // 附加的认证信息
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler); // 成功处理器
        filter.setAuthenticationFailureHandler(authenticationFailureHandler); // 失败处理器
        filter.setSecurityProperties(securityProperties);
//        filter.setFilterProcessesUrl(""); // 映射路径
        return filter;
    }

    /**
     * 会员 认证过滤器
     */
    @Bean
    public MemberAuthenticationFilter memberAuthenticationFilter() throws Exception {
        MemberAuthenticationFilter filter = new MemberAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationDetailsSource(defaultAuthenticationDetailsSource());
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        filter.setSecurityProperties(securityProperties);
        return filter;
    }


    //-------------------------- 前台提交的 额外认证信息 --------------------------

    /**
     * 前台提交的 额外认证信息
     */
    @Bean
    public DefaultAuthenticationDetailsSource defaultAuthenticationDetailsSource() {
        return new DefaultAuthenticationDetailsSource();
    }

    /**
     * 解析所有控制器方法 筛选匿名路径
     */
    private Map<String, Set<String>> getAnonymousUrl(Map<RequestMappingInfo, HandlerMethod> handlerMethodMap) {
        // 匿名路径
        Map<String, Set<String>> anonymousUrls = new HashMap<>(6);
        Set<String> get = new HashSet<>();
        Set<String> post = new HashSet<>();
        Set<String> put = new HashSet<>();
        Set<String> patch = new HashSet<>();
        Set<String> delete = new HashSet<>();
        Set<String> all = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue(); // 控制器中的方法
            AnonymousAccess anonymousAccess = handlerMethod.getMethodAnnotation(AnonymousAccess.class); // 判断方法是否含有匿名标记
            if (null != anonymousAccess) {
                List<RequestMethod> requestMethods = new ArrayList<>(infoEntry.getKey().getMethodsCondition().getMethods()); // 当前方法对应的请求方式集合 [GET]
                RequestMethodEnum request = RequestMethodEnum.find(requestMethods.size() == 0 ? RequestMethodEnum.ALL.getType() : requestMethods.get(0).name()); // 请求方式
                switch (Objects.requireNonNull(request)) {
                    case GET:
                        get.addAll(infoEntry.getKey().getPatternsCondition().getPatterns()); // 当前方法对应的请求路径
                        break;
                    case POST:
                        post.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                        break;
                    case PUT:
                        put.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                        break;
                    case PATCH:
                        patch.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                        break;
                    case DELETE:
                        delete.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                        break;
                    default:
                        // 未指明请求方式 直接放行
                        all.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                        break;
                }
            }
        }
        // 缓存匿名路径
        anonymousUrls.put(RequestMethodEnum.GET.getType(), get);
        anonymousUrls.put(RequestMethodEnum.POST.getType(), post);
        anonymousUrls.put(RequestMethodEnum.PUT.getType(), put);
        anonymousUrls.put(RequestMethodEnum.PATCH.getType(), patch);
        anonymousUrls.put(RequestMethodEnum.DELETE.getType(), delete);
        anonymousUrls.put(RequestMethodEnum.ALL.getType(), all);
        return anonymousUrls;
    }
}
