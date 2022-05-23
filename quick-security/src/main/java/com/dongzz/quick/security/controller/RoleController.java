package com.dongzz.quick.security.controller;

import com.dongzz.quick.common.base.BaseController;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.domain.ResponseVo;
import com.dongzz.quick.security.domain.SysRole;
import com.dongzz.quick.security.service.RoleService;
import com.dongzz.quick.security.service.dto.RoleDto;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 角色
 */
@RestController
@RequestMapping("/api/roles")
@Api(tags = "系统：角色管理", value = "角色管理相关接口")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService; // 角色接口

    /**
     * 新增角色
     */
    @PostMapping
    @ApiOperation(value = "新增角色", notes = "新增角色")
    public ResponseVo add(@ApiParam(name = "角色实体", value = "参数体", required = true) @RequestBody RoleDto roleDto) throws Exception {
        roleService.addRole(roleDto);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 删除角色，逻辑，支持批量
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除角色", notes = "删除角色，逻辑，支持批量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色ID 如：1 或 1,2", dataType = "string", paramType = "path", required = true)
    })
    public ResponseVo delete(@PathVariable("id") String id) throws Exception {
        roleService.deleteRole(id);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 修改角色
     */
    @PutMapping
    @ApiOperation(value = "修改角色", notes = "修改角色")
    public ResponseVo update(@ApiParam(name = "角色实体", value = "参数体", required = true) @RequestBody RoleDto roleDto) throws Exception {
        roleService.updateRole(roleDto);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 角色授予权限
     */
    @PutMapping("/grant")
    @ApiOperation("角色赋予权限")
    public ResponseVo grant(@ApiParam(name = "角色实体", value = "参数体", required = true) @RequestBody RoleDto roleDto) throws Exception {
        roleService.grant(roleDto);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), null);
    }

    /**
     * 获取 角色列表
     */
    @GetMapping
    @ApiOperation(value = "获取角色列表", notes = "支持分页，排序，条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页单位", dataType = "string", paramType = "query")
    })
    public VueTableResponse listRoles(@ApiIgnore VueTableRequest request) throws Exception {
        VueTableResponse response = roleService.findAll(request);
        return response;
    }

    /**
     * 获取 角色详情
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取角色详情", notes = "获取角色详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色ID", dataType = "integer", paramType = "path", required = true)
    })
    public ResponseVo listRole(@PathVariable("id") Integer id) throws Exception {
        RoleDto roleDto = roleService.findById(id);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), roleDto);
    }

    /**
     * 获取 所有角色
     */
    @GetMapping("/all")
    @ApiOperation(value = "角色列表", notes = "角色列表")
    public ResponseVo listRoles() throws Exception {
        List<SysRole> list = roleService.findAll();
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), list);
    }

    /**
     * 获取 指定用户 所有角色
     */
    @GetMapping("/user/roles/{id}")
    @ApiOperation(value = "获取用户角色", notes = "获取用户角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", dataType = "integer", paramType = "path", required = true)
    })
    public ResponseVo listRoles(@PathVariable("id") Integer id) throws Exception {
        List<SysRole> roles = roleService.findRolesByUserId(id);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), roles);
    }

    /**
     * 导出 Excel
     */
    @GetMapping("/export")
    @ApiOperation(value = "导出角色", notes = "导出角色")
    public void export(@ApiIgnore VueTableRequest request) throws Exception {
        VueTableResponse vtp = roleService.findAll(request);
        List<RoleDto> roleDtos = (List<RoleDto>) vtp.getData();
        roleService.download(roleDtos, response);
    }

}
