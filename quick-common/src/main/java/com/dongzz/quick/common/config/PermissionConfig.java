package com.dongzz.quick.common.config;

import com.dongzz.quick.common.utils.SecurityUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义权限匹配器 权限控制
 */
@Service("ql")
public class PermissionConfig {

    /**
     * 权限验证
     */
    public Boolean check(String... permissions) {
        // 获取在线用户权限集合
        List<String> qlPermissions = SecurityUtil.getCurrentUser().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        // 管理员直接放行
        return qlPermissions.contains("ROLE_ADMIN") || Arrays.stream(permissions).anyMatch(qlPermissions::contains);
    }

}
