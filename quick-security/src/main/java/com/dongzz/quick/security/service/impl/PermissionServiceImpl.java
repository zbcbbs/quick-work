package com.dongzz.quick.security.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dongzz.quick.common.base.BaseMybatisServiceImpl;
import com.dongzz.quick.common.exception.ServiceException;
import com.dongzz.quick.common.plugin.vuetables.VueTableHandler;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.utils.StringUtil;
import com.dongzz.quick.common.utils.Util;
import com.dongzz.quick.security.dao.SysPermissionMapper;
import com.dongzz.quick.security.dao.SysRoleMapper;
import com.dongzz.quick.security.domain.SysPermission;
import com.dongzz.quick.security.domain.SysRole;
import com.dongzz.quick.security.domain.vo.MenuMetaVo;
import com.dongzz.quick.security.domain.vo.MenuVo;
import com.dongzz.quick.security.service.PermissionService;
import com.dongzz.quick.security.service.dto.PermissionDto;
import com.dongzz.quick.security.service.mapstruct.PermissionMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PermissionServiceImpl extends BaseMybatisServiceImpl<SysPermission> implements PermissionService {

    @Autowired
    private SysPermissionMapper permissionMapper;
    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapMapper; // 实体映射

    @Override
    public void addPermission(SysPermission permission) throws Exception {
        SysPermission p = permissionMapper.selectPermissionByPname(permission.getName());
        if (null != p) {
            throw new ServiceException("资源名已存在");
        }
        permission.setCreateTime(new Date());
        permission.setUpdateTime(new Date());
        permission.setIsDel("0");
        // 新增
        permissionMapper.insertSelective(permission);

        // 判断 存在父节点
        if (null != permission.getParentId() && !permission.getParentId().equals(0)) {
            Integer subCount = permissionMapper.coutByPid(permission.getParentId());
            permissionMapper.updateSubCount(permission.getParentId(), subCount);
        }
    }

    @Override
    public void deletePermission(String id) throws Exception {
        if (StringUtils.isNotBlank(id)) {
            // 判断 是否批量删除
            if (id.contains(",")) {
                List<String> ids = Util.strSplitToList(id, ",");
                for (String resId : ids) {
                    // 逻辑删除
                    permissionMapper.deletePermission(Integer.parseInt(resId));
                }
            } else {
                permissionMapper.deletePermission(Integer.parseInt(id));
            }
        }

    }

    @Override
    public void updatePermission(SysPermission permission) throws Exception {
        // 校验资源名
        SysPermission p = permissionMapper.selectPermissionByPname(permission.getName());
        if ((null != p) && (!p.getId().equals(permission.getId()))) {
            throw new ServiceException("资源名已存在");
        }
        permission.setUpdateTime(new Date());
        permissionMapper.updateByPrimaryKeySelective(permission);

        // 判断 存在父节点
        if (null != permission.getParentId() && !permission.getParentId().equals(0)) {
            Integer subCount = permissionMapper.coutByPid(permission.getParentId());
            permissionMapper.updateSubCount(permission.getParentId(), subCount);
        }
    }

    @Override
    public List<PermissionDto> findMenusByUserId(Integer id) throws Exception {
        List<SysPermission> permissions = permissionMapper.selectMenusByUserId(id);
        return permissions.stream().map(permissionMapMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<PermissionDto> findMenusByMemberId(Integer id) throws Exception {
        List<SysPermission> permissions = permissionMapper.selectMenusByMemberId(id);
        return permissions.stream().map(permissionMapMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<SysPermission> findPermissionsByUserId(Integer id) throws Exception {
        return permissionMapper.selectPermissionsByUserId(id);
    }

    @Override
    public List<SysPermission> findPermissionsByMemberId(Integer id) throws Exception {
        return permissionMapper.selectPermissionsByMemberId(id);
    }

    @Override
    public List<SysPermission> findPermissionsByRoleId(Integer id) throws Exception {
        return permissionMapper.selectPermissionsByRoleId(id);
    }

    @Override
    public Map<String, Set<String>> findAllPermissionsByUserId(Integer id) throws Exception {
        Map<String, Set<String>> map = new HashMap<>();
        Set<String> allRoles = new HashSet<>();
        Set<String> allPermissions = new HashSet<>();
        // 获取 角色码
        List<SysRole> roles = roleMapper.selectRolesByUserId(id);
        if (!CollectionUtils.isEmpty(roles)) {
            for (SysRole role : roles) {
                allRoles.add(role.getRole());
            }
        }
        // 获取 权限码
        List<SysPermission> permissions = permissionMapper.selectPermissionsByUserId(id);
        if (!CollectionUtils.isEmpty(permissions)) {
            for (SysPermission permission : permissions) {
                allPermissions.add(permission.getPermission());
            }
        }

        map.put("allRoles", allRoles);
        map.put("allPermissions", allPermissions);
        return map;
    }

    @Override
    public Map<String, Set<String>> findAllPermissionsByMemberId(Integer id) throws Exception {
        Map<String, Set<String>> map = new HashMap<>();
        Set<String> allRoles = new HashSet<>();
        Set<String> allPermissions = new HashSet<>();
        // 获取 角色码
        List<SysRole> roles = roleMapper.selectRolesByMemberId(id);
        if (!CollectionUtils.isEmpty(roles)) {
            for (SysRole role : roles) {
                allRoles.add(role.getRole());
            }
        }
        // 获取 权限码
        List<SysPermission> permissions = permissionMapper.selectPermissionsByMemberId(id);
        if (!CollectionUtils.isEmpty(permissions)) {
            for (SysPermission permission : permissions) {
                allPermissions.add(permission.getPermission());
            }
        }

        map.put("allRoles", allRoles);
        map.put("allPermissions", allPermissions);
        return map;
    }

    @Override
    public List<SysPermission> findAllPermissions() throws Exception {
        return permissionMapper.selectPermissions();
    }

    @Override
    public VueTableResponse findAll(VueTableRequest request) throws Exception {
        VueTableHandler handler = new VueTableHandler(new VueTableHandler.CountHandler() {

            @Override
            public int count(VueTableRequest request) {
                try {
                    return permissionMapper.count(request.getParams());
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        }, new VueTableHandler.ListHandler() {

            @Override
            public List<?> list(VueTableRequest request) {
                try {
                    List<SysPermission> permissions = permissionMapper.selectAllPermissions(request.getParams(), request.getOffset(), request.getLimit());
                    // 构造树形结构
                    List<PermissionDto> trees = new ArrayList<>();
                    buildTree(permissionMapMapper.toDto(permissions), trees);
                    return trees;
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ArrayList<>();
                }
            }
        }, new VueTableHandler.OrderHandler() {

            @Override
            public VueTableRequest order(VueTableRequest request) {
                return request;
            }
        });
        VueTableResponse response = handler.handle(request);
        return response;
    }

    @Override
    public List<PermissionDto> findByPid(Integer pid) throws Exception {
        return permissionMapMapper.toDto(permissionMapper.selectByPid(pid));
    }

    @Override
    public void setPermissionsTree(Integer parentId, List<SysPermission> all, JSONArray array) throws Exception {
        for (SysPermission permission : all) {
            if (permission.getParentId().equals(parentId)) {
                String jsonString = JSONObject.toJSONString(permission);
                JSONObject parent = (JSONObject) JSONObject.parse(jsonString);
                array.add(parent);

                // 递归算法 子节点
                JSONArray childArray = new JSONArray();
                parent.put("children", childArray);
                setPermissionsTree(permission.getId(), all, childArray);
            }
        }
    }


    @Override
    public void setPermissionsSorted(Integer parentId, List<SysPermission> all, List<SysPermission> sorted) throws Exception {
        for (SysPermission permission : all) {
            if (permission.getParentId().equals(parentId)) {
                sorted.add(permission);
                // 递归 子节点
                setPermissionsSorted(permission.getId(), all, sorted);
            }
        }
    }

    @Override
    public void buildTree(Integer parentId, List<PermissionDto> all, List<PermissionDto> trees) throws Exception {
        for (PermissionDto permissionDto : all) {
            if (permissionDto.getParentId().equals(parentId)) {
                trees.add(permissionDto);
                // 递归算法 筛选子节点
                List<PermissionDto> children = new ArrayList<>();
                permissionDto.setChildren(children);
                buildTree(permissionDto.getId(), all, children);
            }
        }
    }

    @Override
    public List<MenuVo> buildVueRoutes(List<PermissionDto> permissionDtos) throws Exception {
        List<MenuVo> list = new LinkedList<>();
        permissionDtos.forEach(permissionDto -> {
                    if (permissionDto != null) {
                        List<PermissionDto> permissionDtoList = permissionDto.getChildren();
                        MenuVo menuVo = new MenuVo();
                        menuVo.setName(ObjectUtil.isNotEmpty(permissionDto.getName()) ? permissionDto.getName() : permissionDto.getTitle());
                        // 一级目录需要加斜杠，不然会报警告
                        menuVo.setPath(permissionDto.getParentId() == 0 ? "/" + permissionDto.getHref() : permissionDto.getHref());
                        menuVo.setHidden(permissionDto.getHidden());
                        // 若不是外链
                        if (!permissionDto.getIFrame()) {
                            if (permissionDto.getParentId() == 0) { // 一级菜单
                                menuVo.setComponent(StringUtil.isEmpty(permissionDto.getComponent()) ? "Layout" : permissionDto.getComponent());
                                // 若不是一级菜单，并且菜单类型为目录，则代表是多级菜单，需要手动添加路由占位符来显示子节点内容
                            } else if ("1".equals(permissionDto.getType())) { // 中间目录
                                menuVo.setComponent(StringUtil.isEmpty(permissionDto.getComponent()) ? "ParentView" : permissionDto.getComponent());
                            } else if (StringUtil.isNotBlank(permissionDto.getComponent())) {
                                menuVo.setComponent(permissionDto.getComponent()); // 普通菜单
                            }
                        }
                        menuVo.setMeta(new MenuMetaVo(permissionDto.getTitle(), permissionDto.getIcon(), !permissionDto.getCache()));

                        if (CollectionUtil.isNotEmpty(permissionDtoList)) {
                            menuVo.setAlwaysShow(true);
                            menuVo.setRedirect("noredirect");
                            try {
                                menuVo.setChildren(buildVueRoutes(permissionDtoList));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // 一级菜单且没有子菜单的情况
                        } else if (permissionDto.getParentId() == 0) {
                            MenuVo menuVo1 = new MenuVo();
                            menuVo1.setMeta(menuVo.getMeta());
                            // 非外链
                            if (!permissionDto.getIFrame()) {
                                menuVo1.setPath("index");
                                menuVo1.setName(menuVo.getName());
                                menuVo1.setComponent(menuVo.getComponent());
                            } else {
                                menuVo1.setPath(permissionDto.getHref());
                            }
                            menuVo.setName(null);
                            menuVo.setMeta(null);
                            menuVo.setComponent("Layout");
                            List<MenuVo> list1 = new ArrayList<>();
                            list1.add(menuVo1);
                            menuVo.setChildren(list1);
                        }
                        list.add(menuVo);
                    }
                }
        );
        return list;
    }

    @Override
    public void buildTree(List<PermissionDto> all, List<PermissionDto> trees) throws Exception {
        // 过滤根节点
        for (PermissionDto permissionDto : all) {
            boolean hasPid = false;
            for (PermissionDto p : all) {
                if (permissionDto.getParentId().equals(p.getId())) {
                    hasPid = true;
                    break;
                }
            }
            if (!hasPid) {
                trees.add(permissionDto);
            }
        }

        // 遍历根节点 构造树结构
        for (PermissionDto permissionDto : trees) {
            // 获取子节点 递归调用
            List<PermissionDto> children = getChildren(permissionDto.getId(), all);
            permissionDto.setChildren(children);
        }
    }

    /**
     * 获取子节点
     *
     * @param id  父节点ID
     * @param all 所有节点
     * @return
     */
    private List<PermissionDto> getChildren(Integer id, List<PermissionDto> all) {
        List<PermissionDto> children = new ArrayList<>();
        for (PermissionDto permissionDto : all) {
            if (permissionDto.getParentId().equals(id)) {
                children.add(permissionDto);
            }
        }

        // 递归调用
        for (PermissionDto permissionDto : children) {
            permissionDto.setChildren(getChildren(permissionDto.getId(), all));
        }

        // 若无子节点，添加空集合，递归退出
        if (children.size() == 0) {
            return new ArrayList<>();
        }
        return children;
    }

    @Override
    public Set<SysPermission> getChild(List<SysPermission> child, Set<SysPermission> all) throws Exception {
        for (SysPermission permission : child) {
            all.add(permission);
            List<SysPermission> c = permissionMapper.selectByPid(permission.getId());
            if (null != c && c.size() > 0) {
                getChild(c, all); // 递归调用
            }
        }
        return all;
    }

    @Override
    public Set<SysPermission> getParent(SysPermission parent, Set<SysPermission> all) throws Exception {
        if (parent != null) {
            all.add(parent);
            SysPermission p = permissionMapper.selectByPrimaryKey(parent.getParentId()); // 上级
            getParent(p, all); // 递归调用
        }

        return all;
    }
}
