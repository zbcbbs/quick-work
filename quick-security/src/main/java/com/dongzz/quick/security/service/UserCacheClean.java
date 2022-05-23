package com.dongzz.quick.security.service;

import com.dongzz.quick.security.service.impl.MemberDetailsServiceImpl;
import com.dongzz.quick.security.service.impl.UserDetailsServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 用户缓存清理
 * 用户登录信息缓存，为防止Spring循环依赖与安全考虑 ，单独构成工具类
 */
@Component
public class UserCacheClean {


    /**
     * 清理特定用户缓存信息
     * 用户信息变更时
     *
     * @param username /
     */
    public void cleanUserCache(String username) {
        if (StringUtils.isNotEmpty(username)) {
            UserDetailsServiceImpl.userCache.remove(username);
        }
    }

    public void cleanMemCache(String username) {
        if (StringUtils.isNotEmpty(username)) {
            MemberDetailsServiceImpl.userCache.remove(username);
        }
    }

    /**
     * 清理所有用户的缓存信息
     * 如发生角色授权信息变化，可以简便的全部失效缓存
     */
    public void cleanUserAll() {
        UserDetailsServiceImpl.userCache.clear();
    }

    public void cleanMemAll() {
        MemberDetailsServiceImpl.userCache.clear();
    }
}
