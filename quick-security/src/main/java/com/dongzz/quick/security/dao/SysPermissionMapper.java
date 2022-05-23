package com.dongzz.quick.security.dao;

import com.dongzz.quick.common.base.BaseMybatisMapper;
import com.dongzz.quick.security.domain.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 资源 数据访问接口
 */
public interface SysPermissionMapper extends BaseMybatisMapper<SysPermission> {

    /**
     * 删除资源，逻辑删除
     *
     * @param resId 资源ID
     * @throws Exception
     */
    void deletePermission(Integer resId) throws Exception;

    /**
     * 根据资源名 查询
     *
     * @param resName 资源名
     * @return
     * @throws Exception
     */
    SysPermission selectPermissionByPname(String resName) throws Exception;

    /**
     * 根据用户ID 查询菜单
     *
     * @param id 用户ID
     * @return
     * @throws Exception
     */
    List<SysPermission> selectMenusByUserId(Integer id) throws Exception;

    /**
     * 根据会员ID 查询菜单
     *
     * @param id 会员ID
     * @return
     * @throws Exception
     */
    List<SysPermission> selectMenusByMemberId(Integer id) throws Exception;

    /**
     * 根据用户ID 查询按钮权限
     *
     * @param id 用户ID
     * @return
     * @throws Exception
     */
    List<SysPermission> selectPermissionsByUserId(Integer id) throws Exception;

    /**
     * 根据会员ID 查询按钮权限
     *
     * @param id 会员ID
     * @return
     * @throws Exception
     */
    List<SysPermission> selectPermissionsByMemberId(Integer id) throws Exception;

    /**
     * 根据角色ID 查询角色持有的资源
     *
     * @param roleId 角色ID
     * @return
     * @throws Exception
     */
    List<SysPermission> selectPermissionsByRoleId(Integer roleId) throws Exception;

    /**
     * 查询 所有资源
     *
     * @return
     * @throws Exception
     */
    List<SysPermission> selectPermissions() throws Exception;

    /**
     * 根据PID查询
     *
     * @param pid
     * @return
     * @throws Exception
     */
    List<SysPermission> selectByPid(Integer pid) throws Exception;

    /**
     * 查询父节点的子节点数
     *
     * @param pid 父节点
     * @return
     * @throws Exception
     */
    Integer coutByPid(Integer pid) throws Exception;

    /**
     * 修改指定节点的子节点数量
     *
     * @param id       节点ID
     * @param subCount 数量
     * @throws Exception
     */
    void updateSubCount(@Param("id") Integer id, @Param("subCount") Integer subCount) throws Exception;

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
    List<SysPermission> selectAllPermissions(@Param("params") Map<String, Object> params,
                                             @Param("offset") Integer offset, @Param("limit") Integer limit) throws Exception;
}