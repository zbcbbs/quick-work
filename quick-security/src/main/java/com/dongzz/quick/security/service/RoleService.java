package com.dongzz.quick.security.service;

import com.dongzz.quick.common.base.BaseMybatisService;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.security.domain.SysRole;
import com.dongzz.quick.security.service.dto.RoleDto;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 角色 相关服务接口
 */
public interface RoleService extends BaseMybatisService<SysRole> {

    /**
     * 新增角色
     *
     * @param role 角色
     * @throws Exception
     */
    void addRole(SysRole role) throws Exception;

    /**
     * 新增角色
     *
     * @param roleDto 角色拓展
     * @throws Exception
     */
    void addRole(RoleDto roleDto) throws Exception;

    /**
     * 修改角色
     *
     * @param roleDto 角色拓展
     * @throws Exception
     */
    void updateRole(RoleDto roleDto) throws Exception;

    /**
     * 角色授权
     *
     * @param roleDto 必需包含 角色ID 和 权限集合
     * @throws Exception
     */
    void grant(RoleDto roleDto) throws Exception;

    /**
     * 删除角色，逻辑，支持批量
     *
     * @param id 1 或 1,2
     * @throws Exception
     */
    void deleteRole(String id) throws Exception;

    /**
     * 修改角色
     *
     * @param role 角色
     * @throws Exception
     */
    void updateRole(SysRole role) throws Exception;

    /**
     * 获取 用户持有的角色
     *
     * @param id 用户ID
     * @return
     * @throws Exception
     */
    List<SysRole> findRolesByUserId(Integer id) throws Exception;

    /**
     * 获取 所有角色
     *
     * @return
     * @throws Exception
     */
    List<SysRole> findAll() throws Exception;

    /**
     * 分页 条件查询
     *
     * @param request 分页请求
     * @return
     * @throws Exception
     */
    VueTableResponse findAll(VueTableRequest request) throws Exception;

    /**
     * 根据ID查询角色
     *
     * @param id
     * @return
     * @throws Exception
     */
    RoleDto findById(Integer id) throws Exception;

    /**
     * 下载导出 Excel
     *
     * @param roleDtos 数据
     * @param response
     * @throws Exception
     */
    void download(List<RoleDto> roleDtos, HttpServletResponse response) throws Exception;

}
