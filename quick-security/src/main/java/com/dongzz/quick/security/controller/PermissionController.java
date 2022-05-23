package com.dongzz.quick.security.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.dongzz.quick.common.base.BaseController;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.utils.SecurityUtil;
import com.dongzz.quick.common.domain.ResponseVo;
import com.dongzz.quick.security.domain.SysPermission;
import com.dongzz.quick.security.domain.vo.MenuVo;
import com.dongzz.quick.security.service.PermissionService;
import com.dongzz.quick.security.service.dto.PermissionDto;
import com.dongzz.quick.security.service.mapstruct.PermissionMapper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 资源权限
 */
@RestController
@RequestMapping("/api/permissions")
@Api(tags = "系统：资源管理", value = "资源管理相关接口")
public class PermissionController extends BaseController {

    @Autowired
    private PermissionService permissionService; // 权限接口
    @Autowired
    private PermissionMapper permissionMapMapper;

    /**
     * 新增资源
     */
    @PostMapping
    @ApiOperation(value = "新增资源", notes = "新增资源")
    public ResponseVo add(@ApiParam(name = "资源实体", value = "参数体", required = true) @RequestBody SysPermission permission) throws Exception {
        permissionService.addPermission(permission);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 删除资源，逻辑，支持批量
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除资源", notes = "删除资源，逻辑，支持批量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "资源ID 如：1 或 1,2", dataType = "string", paramType = "path", required = true)
    })
    public ResponseVo delete(@PathVariable("id") String id) throws Exception {
        permissionService.deletePermission(id);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 修改资源
     */
    @PutMapping
    @ApiOperation(value = "修改资源", notes = "修改资源")
    public ResponseVo update(@ApiParam(name = "资源实体", value = "参数体", required = true) @RequestBody SysPermission permission) throws Exception {
        permissionService.updatePermission(permission);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 获取资源列表
     */
    @GetMapping
    @ApiOperation(value = "获取资源列表", notes = "支持分页，排序，条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页单位", dataType = "string", paramType = "query")
    })
    public VueTableResponse listPermissions(@ApiIgnore VueTableRequest request) throws Exception {
        VueTableResponse response = permissionService.findAll(request);
        return response;
    }

    /**
     * 获取同级和上级节点
     */
    @PostMapping("/superior")
    @ApiOperation("获取同级与上级节点")
    public ResponseVo superior(@RequestBody List<Integer> ids) throws Exception {
        Set<SysPermission> all = new LinkedHashSet<>();
        if (CollectionUtil.isNotEmpty(ids)) {
            for (Integer id : ids) {
                SysPermission self = permissionService.selectByPk(id);
                all.add(self);
                SysPermission parent = permissionService.selectByPk(self.getParentId());
                all = permissionService.getParent(parent, all);
            }
        }
        List<PermissionDto> trees = new ArrayList<>();
        permissionService.buildTree(permissionMapMapper.toDto(new ArrayList<>(all)), trees);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), trees);
    }

    /**
     * 根据PID查询
     */
    @GetMapping("/lazy")
    @ApiOperation("根据PID查询子节点")
    public ResponseVo lazy(Integer pid) throws Exception {
        List<PermissionDto> permissionDtos = permissionService.findByPid(pid);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), permissionDtos);
    }

    /**
     * 查询选中节点的所有子节点 包含自身
     */
    @GetMapping("/child")
    @ApiOperation("查询所有子节点，包含自身ID")
    public ResponseVo child(Integer id) throws Exception {
        Set<SysPermission> all = new HashSet<>();
        List<PermissionDto> child = permissionService.findByPid(id); // 子节点
        all.add(permissionService.selectByPk(id)); // 自身
        all = permissionService.getChild(permissionMapMapper.toEntity(child), all);
        Set<Integer> ids = all.stream().map(SysPermission::getId).collect(Collectors.toSet());
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), ids);
    }

    /**
     * 查询选中节点的所有父节点 包含自身
     */
    @GetMapping("/parent")
    @ApiOperation("查询所有父节点，包含自身ID")
    public ResponseVo parent(Integer id) throws Exception {
        Set<SysPermission> all = new HashSet<>();
        SysPermission self = permissionService.selectByPk(id);
        all.add(self); // 自身
        SysPermission parent = permissionService.selectByPk(self.getParentId());
        all = permissionService.getParent(parent, all);
        Set<Integer> ids = all.stream().map(SysPermission::getId).collect(Collectors.toSet());
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), ids);
    }

    /**
     * 查询选中节点的所有关联节点 包含自身
     */
    @GetMapping("/checked")
    @ApiOperation("查询选中节点的所有关联节点 包含自身ID")
    public ResponseVo checked(Integer id) throws Exception {
        SysPermission self = permissionService.selectByPk(id); // 自身
        // 关联父节点
        Set<SysPermission> parentSet = new HashSet<>();
        parentSet.add(self);
        SysPermission parent = permissionService.selectByPk(self.getParentId());
        parentSet = permissionService.getParent(parent, parentSet);
        Set<Integer> parentIds = parentSet.stream().map(SysPermission::getId).collect(Collectors.toSet());
        // 关联子节点
        Set<SysPermission> childSet = new HashSet<>();
        childSet.add(self);
        List<PermissionDto> child = permissionService.findByPid(id);
        childSet = permissionService.getChild(permissionMapMapper.toEntity(child), childSet);
        Set<Integer> childIds = childSet.stream().map(SysPermission::getId).collect(Collectors.toSet());
        // 合并 去重
        Collection<Integer> ids = CollUtil.addAll(parentIds, childIds);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), ids);
    }

    /**
     * 获取 资源详情
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取资源详情", notes = "查询资源详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "资源ID", dataType = "integer", paramType = "path", required = true)
    })
    public ResponseVo listPermission(@PathVariable("id") Integer id) throws Exception {
        SysPermission p = permissionService.selectByPk(id);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), p);
    }

    /**
     * 查询用户菜单，构造前端路由
     */
    @GetMapping("/build")
    @ApiOperation("查询用户菜单，构造前端路由")
    public ResponseVo build() throws Exception {
        List<PermissionDto> permissionDtos = permissionService.findMenusByUserId(SecurityUtil.getCurrentUserId()); // 查询
        List<PermissionDto> trees = new ArrayList<>(); // 构造树形结构
        permissionService.buildTree(0, permissionDtos, trees);
        List<MenuVo> menuVos = permissionService.buildVueRoutes(trees); // 前端路由
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), menuVos);
    }

    /**
     * 获取 指定角色 持有的资源
     */
    @GetMapping("/role/{id}")
    @ApiOperation(value = "获取角色持有的资源资源", notes = "获取角色持有的资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色ID", dataType = "integer", paramType = "path", required = true)
    })
    public ResponseVo listRoleOwnPermissions(@PathVariable("id") Integer id) throws Exception {
        List<SysPermission> permissions = permissionService.findPermissionsByRoleId(id);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), permissions);
    }

    /**
     * 获取 所有资源
     */
    @GetMapping("/all")
    @ApiOperation(value = "获取所有资源", notes = "获取所有资源")
    public ResponseVo listAllPermissions() throws Exception {
        List<SysPermission> permissions = permissionService.findAllPermissions();
        List<SysPermission> sorted = new ArrayList<SysPermission>();
        // 构建树形结构，父子节点就近排序
        permissionService.setPermissionsSorted(0, permissions, sorted);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), sorted);
    }


}
