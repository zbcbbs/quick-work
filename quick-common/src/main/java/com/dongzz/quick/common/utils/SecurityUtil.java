package com.dongzz.quick.common.utils;

import cn.hutool.json.JSONObject;
import com.dongzz.quick.common.exception.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限工具
 */
public class SecurityUtil {

    /**
     * 获取 当前登录的用户
     */
    public static UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AuthenticationException(HttpStatus.UNAUTHORIZED, "当前登录状态过期");
        }
        // 匿名登录
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        // 账密登录
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails;
        }
        return null;
    }

    /**
     * 获取 当前登录的用户ID
     *
     * @return 用户ID
     */
    public static Integer getCurrentUserId() {
        UserDetails userDetails = getCurrentUser();
        return new JSONObject(userDetails).get("id", Integer.class);
    }

    /**
     * 获取 当前登录的 用户权限集合
     *
     * @return 角色码+权限码
     */
    public static Set<String> getCurrentUserPermission() {
        UserDetails userDetails = getCurrentUser();
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) userDetails.getAuthorities();
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

    }

}
