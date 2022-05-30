package com.dongzz.quick.security.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.dongzz.quick.common.utils.RegexUtil;
import com.dongzz.quick.security.config.bean.LoginProperties;
import com.dongzz.quick.security.domain.SysUser;
import com.dongzz.quick.security.service.PermissionService;
import com.dongzz.quick.security.service.UserService;
import com.dongzz.quick.security.service.dto.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理员 账号认证接口
 */
@Service("UserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    public static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private LoginProperties loginProperties;

    // 用户信息缓存
    public static ConcurrentHashMap<String, LoginUser> userCache = new ConcurrentHashMap<>();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("User detail service： loadUserByUsername={}", username);

        boolean searchDb = true; // 是否查询数据库
        LoginUser loginUser = null;

        // 判断是否启用缓存
        if (loginProperties.isCacheEnable() && userCache.containsKey(username)) {
            // 直接从缓存中拉取用户
            loginUser = userCache.get(username);
            searchDb = false;
        }

        // 查询数据库
        if (searchDb) {
            SysUser sysUser = null;
            String error = "账号不存在";
            try {
                // 正则验证 验证前台提交的账号格式
                if (RegexUtil.checkMobile(username)) { // 手机号
                    sysUser = userService.findUserByUphone(username);
                    error = "手机号尚未注册";
                } else if (RegexUtil.checkEmail(username)) { // 邮箱
                    sysUser = userService.findUserByUemail(username);
                    error = "邮箱尚未注册";
                } else { // 用户名
                    sysUser = userService.findUserByUname(username);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (sysUser == null) {
                throw new UsernameNotFoundException(error);
            }

            // 构造认证主体
            loginUser = new LoginUser();
            loginUser.setId(sysUser.getId());
            loginUser.setUsername(sysUser.getUsername());
            loginUser.setPassword(sysUser.getPassword());
            loginUser.setAdmin(true); // 管理员
            loginUser.setStatus(sysUser.getStatus());
            loginUser.setUuid(IdUtil.simpleUUID()); // 缓存标记

            // 获取权限码
            Set<String> allPermissions = null;
            Set<String> allRoles = null;
            try {
                Map<String, Set<String>> map = permissionService.findAllPermissionsByUserId(sysUser.getId());
                allPermissions = map.get("allPermissions");
                allRoles = map.get("allRoles");
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 组装权限代码
            Set<String> resources = (Set<String>) CollectionUtil.addAll(allRoles, allPermissions);
            loginUser.setResources(resources);

            // 缓存用户信息
            userCache.put(username, loginUser);
        }

        return loginUser;
    }

}
