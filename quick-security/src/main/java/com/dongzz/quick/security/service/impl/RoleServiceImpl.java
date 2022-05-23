package com.dongzz.quick.security.service.impl;

import com.dongzz.quick.common.base.BaseMybatisServiceImpl;
import com.dongzz.quick.common.exception.ServiceException;
import com.dongzz.quick.common.plugin.vuetables.VueTableHandler;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.utils.DateUtil;
import com.dongzz.quick.common.utils.ExcelUtil;
import com.dongzz.quick.common.utils.Util;
import com.dongzz.quick.security.dao.SysPermissionMapper;
import com.dongzz.quick.security.dao.SysRoleMapper;
import com.dongzz.quick.security.domain.SysPermission;
import com.dongzz.quick.security.domain.SysRole;
import com.dongzz.quick.security.service.RoleService;
import com.dongzz.quick.security.service.dto.RoleDto;
import com.dongzz.quick.security.service.mapstruct.RoleMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Transactional
public class RoleServiceImpl extends BaseMybatisServiceImpl<SysRole> implements RoleService {

    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private SysPermissionMapper permissionMapper;
    @Autowired
    private RoleMapper roleMapMapper;

    @Override
    public void addRole(SysRole role) throws Exception {
        SysRole r = roleMapper.selectRoleByRname(role.getName());
        if (null != r) {
            throw new ServiceException("角色名已存在");
        }
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
        role.setIsDel("0");
        // 新增
        roleMapper.insertSelective(role);
    }

    @Override
    public void addRole(RoleDto roleDto) throws Exception {
        SysRole role = roleDto;
        SysRole r = roleMapper.selectRoleByRname(roleDto.getName());
        if (null != r) {
            throw new ServiceException("角色名已存在");
        }
        // 默认值
        role.setIsDel("0");
        // 新增 获取新增主键
        roleMapper.insertOne(role);

        // 角色授权
        List<Integer> permissions = roleDto.getPermissions();
        if (!CollectionUtils.isEmpty(permissions)) {
            roleMapper.insertRolePermissions(role.getId(), permissions);
        }
    }

    @Override
    public void updateRole(RoleDto roleDto) throws Exception {
        SysRole role = roleDto;
        SysRole r = roleMapper.selectRoleByRname(roleDto.getName());
        if (null != r && (r.getId() != roleDto.getId())) {
            throw new ServiceException("角色名已存在");
        }

        role.setUpdateTime(new Date());
        // 修改
        roleMapper.updateByPrimaryKeySelective(role);

        // 角色授权
        List<Integer> permissions = roleDto.getPermissions();
        if (!CollectionUtils.isEmpty(permissions)) {
            //删除 原有权限
            roleMapper.deleteRolePermissions(roleDto.getId());
            // 重新授权
            roleMapper.insertRolePermissions(roleDto.getId(), permissions);
        }

    }

    @Override
    public void grant(RoleDto roleDto) throws Exception {
        // 角色授权
        List<Integer> permissions = roleDto.getPermissions();
        if (!CollectionUtils.isEmpty(permissions)) {
            // 清除已有权限
            roleMapper.deleteRolePermissions(roleDto.getId());
            // 重新授权
            roleMapper.insertRolePermissions(roleDto.getId(), permissions);
        }
    }

    @Override
    public void deleteRole(String id) throws Exception {
        if (StringUtils.isNotBlank(id)) {
            // 判断 是否批量删除
            if (id.contains(",")) {
                List<String> ids = Util.strSplitToList(id, ",");
                for (String roleId : ids) {
                    // 逻辑删除
                    roleMapper.deleteRole(Integer.parseInt(roleId));
                }
            } else {
                roleMapper.deleteRole(Integer.parseInt(id));
            }
        }
    }

    @Override
    public void updateRole(SysRole role) throws Exception {
        // 校验角色名
        SysRole r = roleMapper.selectRoleByRname(role.getName());
        if ((null != r) && (r.getId() != role.getId())) {
            throw new ServiceException("角色名已存在");
        }
        role.setUpdateTime(new Date());
        // 更新
        roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public List<SysRole> findRolesByUserId(Integer id) throws Exception {
        return roleMapper.selectRolesByUserId(id);
    }

    @Override
    public List<SysRole> findAll() throws Exception {
        return roleMapper.selectRoles();
    }

    @Override
    public VueTableResponse findAll(VueTableRequest request) throws Exception {
        VueTableHandler handler = new VueTableHandler(new VueTableHandler.CountHandler() {

            @Override
            public int count(VueTableRequest request) {
                try {
                    return roleMapper.count(request.getParams());
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        }, new VueTableHandler.ListHandler() {

            @Override
            public List<?> list(VueTableRequest request) {
                try {
                    List<SysRole> roles = roleMapper.selectAllRoles(request.getParams(), request.getOffset(), request.getLimit());
                    List<RoleDto> list = new ArrayList<>();
                    for (SysRole role : roles) {
                        RoleDto roleDto = roleMapMapper.toDto(role);
                        // 查询角色对应的权限
                        List<SysPermission> permissions = permissionMapper.selectPermissionsByRoleId(role.getId());
                        roleDto.setPermissions(permissions.stream().map(SysPermission::getId).collect(Collectors.toList()));
                        list.add(roleDto);
                    }
                    return list;
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ArrayList<>();
                }
            }
        }, new VueTableHandler.OrderHandler() {

            @Override
            public VueTableRequest order(VueTableRequest request) {
                // 预处理排序
                Map<String, Object> params = request.getParams();
                if (null != params.get("orderBy")) {
                    String orderBy = params.get("orderBy").toString();
                    if (orderBy.contains("id")) {
                        orderBy = orderBy.replace("id", "r.id");
                    }
                    // 重置排序
                    params.put("orderBy", orderBy);
                }
                return request;
            }
        });
        VueTableResponse response = handler.handle(request);
        return response;
    }

    @Override
    public RoleDto findById(Integer id) throws Exception {
        RoleDto roleDto = roleMapMapper.toDto(roleMapper.selectByPrimaryKey(id));
        // 查询角色对应的权限
        List<SysPermission> permissions = permissionMapper.selectPermissionsByRoleId(roleDto.getId());
        roleDto.setPermissions(permissions.stream().map(SysPermission::getId).collect(Collectors.toList()));
        return roleDto;
    }

    @Override
    public void download(List<RoleDto> roleDtos, HttpServletResponse response) throws Exception {
        String[] headers = new String[]{"ID", "角色名", "编码", "类型", "描述", "创建时间"};
        List<Object[]> datas = new ArrayList<>();
        for (RoleDto r : roleDtos) {
            Object[] data = new Object[headers.length];
            data[0] = r.getId();
            data[1] = r.getName();
            data[2] = r.getRole();
            data[3] = r.getType().equals("1") ? "管理员角色" : "会员角色";
            data[4] = r.getDescription();
            data[5] = DateUtil.getDate("yyyy-MM-dd HH:mm:ss", r.getCreateTime());
            datas.add(data);
        }
        // 导出
        ExcelUtil.writeExcel("角色表.xlsx", headers, datas, response);
    }
}
