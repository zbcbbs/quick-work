package com.dongzz.quick.security.dao;

import com.dongzz.quick.common.base.BaseMybatisMapper;
import com.dongzz.quick.security.domain.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 角色 数据访问接口
 */
public interface SysRoleMapper extends BaseMybatisMapper<SysRole> {

    /**
     * 新增角色 获取新增主键
     *
     * @param role 角色
     * @return
     * @throws Exception
     */
    int insertOne(SysRole role) throws Exception;

    /**
     * 角色授权 新增角色权限关系
     *
     * @param roleId        角色ID
     * @param permissionIds 权限ID集合
     * @throws Exception
     */
    void insertRolePermissions(@Param("roleId") Integer roleId,
                               @Param("permissionIds") List<Integer> permissionIds) throws Exception;

    /**
     * 删除角色权限关系
     *
     * @param roleId 角色ID
     * @throws Exception
     */
    void deleteRolePermissions(Integer roleId) throws Exception;

    /**
     * 删除角色，逻辑删除
     *
     * @param roleId 角色ID
     * @throws Exception
     */
    void deleteRole(Integer roleId) throws Exception;

    /**
     * 根据角色名 查询
     *
     * @param roleName 角色名
     * @return
     * @throws Exception
     */
    SysRole selectRoleByRname(String roleName) throws Exception;

    /**
     * 查询 用户 持有的角色
     *
     * @param id 用户ID
     * @return
     * @throws Exception
     */
    List<SysRole> selectRolesByUserId(Integer id) throws Exception;

    /**
     * 查询 会员 持有的角色
     *
     * @param id 会员ID
     * @return
     * @throws Exception
     */
    List<SysRole> selectRolesByMemberId(Integer id) throws Exception;

    /**
     * 查询 所有角色
     *
     * @return
     * @throws Exception
     */
    List<SysRole> selectRoles() throws Exception;

    /**
     * 条件查询 统计数量
     *
     * @param params 查询条件
     * @return
     * @throws Exception
     */
    Integer count(@Param("params") Map<String, Object> params) throws Exception;

    /**
     * 条件查询 分页
     *
     * @param params 查询条件 包含排序
     * @param offset 每页起始索引
     * @param limit  每页显示条数
     * @return
     * @throws Exception
     */
    List<SysRole> selectAllRoles(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
                                 @Param("limit") Integer limit) throws Exception;
}