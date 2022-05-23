package com.dongzz.quick.security.service.dto;

import com.dongzz.quick.security.domain.SysRole;

import java.util.List;

/**
 * 角色 实体 扩展
 */
public class RoleDto extends SysRole {

    private List<Integer> permissions;

    public List<Integer> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Integer> permissions) {
        this.permissions = permissions;
    }

}
