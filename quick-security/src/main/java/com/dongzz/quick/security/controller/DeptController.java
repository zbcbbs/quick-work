package com.dongzz.quick.security.controller;

import com.dongzz.quick.common.base.BaseController;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.domain.ResponseVo;
import com.dongzz.quick.security.domain.SysDept;
import com.dongzz.quick.security.service.DeptService;
import com.dongzz.quick.security.service.dto.DeptDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 部门
 */
@RestController
@RequestMapping("/api/depts")
@Api(tags = "系统：部门管理", value = "部门管理相关接口")
public class DeptController extends BaseController {

    @Autowired
    private DeptService deptService;

    /**
     * 新增部门
     */
    @PostMapping
    @ApiOperation("新增部门")
    public ResponseVo add(@RequestBody SysDept dept) throws Exception {
        deptService.addDept(dept);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 删除部门，支持批量
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除部门，支持批量")
    public ResponseVo delete(@PathVariable("id") String id) throws Exception {
        deptService.deleteDept(id);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 修改部门
     */
    @PutMapping
    @ApiOperation("修改部门")
    public ResponseVo update(@RequestBody SysDept dept) throws Exception {
        deptService.updateDept(dept);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 查询部门列表
     */
    @GetMapping
    @ApiOperation(value = "获取部门列表", notes = "获取部门列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页单位", dataType = "string", paramType = "query")
    })
    public VueTableResponse listDepts(@ApiIgnore VueTableRequest request) throws Exception {
        VueTableResponse response = deptService.findAll(request);
        return response;

    }

    /**
     * 查询部门列表 构造树形结构
     */
    @GetMapping("/tree")
    @ApiOperation(value = "获取部门列表", notes = "构造为树形结构")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页单位", dataType = "string", paramType = "query")
    })
    public VueTableResponse listDeptsTree(@ApiIgnore VueTableRequest request) throws Exception {
        VueTableResponse response = deptService.findAll(request);
        // 构造树形结构
        List<DeptDto> deptDtos = (List<DeptDto>) response.getData();
        List<DeptDto> trees = new ArrayList<>();
        deptService.buildTree(deptDtos, trees);
        response.setData(trees); // 重置
        return response;
    }

    /**
     * 根据ID查询同级和上级部门数据
     */
    @PostMapping("/superior")
    @ApiOperation("根据ID查询同级和上级部门数据")
    public ResponseVo listDeptsSuperior(@RequestBody List<Integer> ids) throws Exception {
        List<DeptDto> trees = new ArrayList<>();
        Set<DeptDto> deptDtos = new LinkedHashSet<>();
        for (Integer id : ids) {
            DeptDto deptDto = deptService.findById(id);
            List<DeptDto> depts = deptService.getSuperior(deptDto, new ArrayList<>());
            deptDtos.addAll(depts);
        }
        // 构造树形结构
        deptService.buildTree(new ArrayList<>(deptDtos), trees);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), trees);
    }

    /**
     * 导出 Excel
     */
    @GetMapping("/export")
    @ApiOperation(value = "导出部门", notes = "导出部门")
    public void export(@ApiIgnore VueTableRequest request) throws Exception {
        VueTableResponse vtp = deptService.findAll(request);
        List<DeptDto> deptDtos = (List<DeptDto>) vtp.getData();
        deptService.download(deptDtos, response);
    }
}
