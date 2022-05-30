package com.dongzz.quick.security.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 认证主体
 */
@Setter
@Getter
@NoArgsConstructor
public class LoginUser implements UserDetails {

    private Integer id;
    private String username;
    private String password;
    private String status;
    private Set<String> resources; // 权限编码

    private boolean isAdmin; // 管理员
    private String uuid; // 缓存标记


    @Override
    @JSONField(serialize = false)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return resources.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 账号未过期
     */
    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账号未锁定
     */
    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonLocked() {
        return !"2".equals(status);
    }

    /**
     * 密码未过期
     */
    @Override
    @JSONField(serialize = false)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账号激活
     */
    @Override
    @JSONField(serialize = false)
    public boolean isEnabled() {
        return "1".equals(status);
    }

}
