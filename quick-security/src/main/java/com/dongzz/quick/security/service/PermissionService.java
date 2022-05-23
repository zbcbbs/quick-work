package com.dongzz.quick.security.service;

import com.alibaba.fastjson.JSONArray;
import com.dongzz.quick.common.base.BaseMybatisService;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.security.domain.SysPermission;
import com.dongzz.quick.security.domain.vo.MenuVo;
import com.dongzz.quick.security.service.dto.PermissionDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 资源 相关服务接口
 */
public interface PermissionService extends BaseMybatisService<SysPermission> {

    /**
     * 新增资源
     *
     * @param permission 资源
     * @throws Exception
     */
    void addPermission(SysPermission permission) throws Exception;

    /**
     * 删除，逻辑，支持批量
     *
     * @param id 资源ID 1 或 1,2
     * @throws Exception
     */
    void deletePermission(String id) throws Exception;

    /**
     * 修改资源
     *
     * @param permission 资源
     * @throws Exception
     */
    void updatePermission(SysPermission permission) throws Exception;

    /**
     * 根据用户ID 查询用户菜单
     *
     * @param id 用户ID
     * @return
     * @throws Exception
     */
    List<PermissionDto> findMenusByUserId(Integer id) throws Exception;

    /**
     * 根据会员ID 查询会员菜单
     *
     * @param id 会员ID
     * @return
     * @throws Exception
     */
    List<PermissionDto> findMenusByMemberId(Integer id) throws Exception;

    /**
     * 根据用户ID 查询用户权限
     *
     * @param id 用户ID
     * @return
     * @throws Exception
     */
    List<SysPermission> findPermissionsByUserId(Integer id) throws Exception;

    /**
     * 根据会员ID 查询会员权限
     *
     * @param id 会员ID
     * @return
     * @throws Exception
     */
    List<SysPermission> findPermissionsByMemberId(Integer id) throws Exception;

    /**
     * 根据角色ID 查询角色持有的 资源
     *
     * @param id 角色ID
     * @return
     * @throws Exception
     */
    List<SysPermission> findPermissionsByRoleId(Integer id) throws Exception;

    /**
     * 根据用户ID 查询用户持有的 角色和权限码
     *
     * @param id 用户ID
     * @return
     * @throws Exception
     */
    Map<String, Set<String>> findAllPermissionsByUserId(Integer id) throws Exception;

    /**
     * 根据会员ID 查询会员持有的 角色和权限码
     *
     * @param id 会员ID
     * @return
     * @throws Exception
     */
    Map<String, Set<String>> findAllPermissionsByMemberId(Integer id) throws Exception;

    /**
     * 查询 所有资源
     *
     * @return
     * @throws Exception
     */
    List<SysPermission> findAllPermissions() throws Exception;

    /**
     * 分页 条件查询
     *
     * @param request 分页请求
     * @return
     * @throws Exception
     */
    VueTableResponse findAll(VueTableRequest request) throws Exception;

    /**
     * 根据PID查询
     *
     * @param pid
     * @return
     * @throws Exception
     */
    List<PermissionDto> findByPid(Integer pid) throws Exception;

    /**
     * 资源筛选，构建树形结构
     *
     * @param parentId 根节点
     * @param all      所有资源
     * @param array    资源集合
     * @throws Exception
     */
    void setPermissionsTree(Integer parentId, List<SysPermission> all, JSONArray array) throws Exception;

    /**
     * 资源筛选，构建树形关系，存在父子关系的资源放在一块
     *
     * @param parentId 起始父节点
     * @param all      所有资源
     * @param sorted   资源集合
     * @throws Exception
     */
    void setPermissionsSorted(Integer parentId, List<SysPermission> all, List<SysPermission> sorted) throws Exception;

    /**
     * 查询菜单 构造 Vue 前端路由
     *
     * @param permissionDtos
     * @return
     * @throws Exception
     */
    List<MenuVo> buildVueRoutes(List<PermissionDto> permissionDtos) throws Exception;

    /**
     * 构建树形结构
     *
     * @param parentId 根节点
     * @param all      所有节点
     * @param trees    树形集合
     * @throws Exception
     */
    void buildTree(Integer parentId, List<PermissionDto> all, List<PermissionDto> trees) throws Exception;

    /**
     * 构建树形结构
     *
     * @param all   所有节点
     * @param trees 树形集合
     * @throws Exception
     */
    void buildTree(List<PermissionDto> all, List<PermissionDto> trees) throws Exception;

    /**
     * 获取选中节点的所有子节点 包含自身
     *
     * @param child 子节点
     * @param all
     * @return
     * @throws Exception
     */
    Set<SysPermission> getChild(List<SysPermission> child, Set<SysPermission> all) throws Exception;

    /**
     * 获取选中节点的所有上级节点 包含自身
     *
     * @param parent 父节点
     * @param all
     * @return
     * @throws Exception
     */
    Set<SysPermission> getParent(SysPermission parent, Set<SysPermission> all) throws Exception;

}
