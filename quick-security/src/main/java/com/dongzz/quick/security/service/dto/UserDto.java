package com.dongzz.quick.security.service.dto;

import com.dongzz.quick.security.domain.SysUser;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 用户 实体 扩展
 */
@Getter
@Setter
public class UserDto extends SysUser {

    private String deptName; // 部门

    private List<Integer> roles; // 角色

}
