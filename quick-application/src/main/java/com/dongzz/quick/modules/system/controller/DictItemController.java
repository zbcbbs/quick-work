package com.dongzz.quick.modules.system.controller;

import com.dongzz.quick.common.base.BaseController;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.domain.ResponseVo;
import com.dongzz.quick.modules.system.domain.SysDictItem;
import com.dongzz.quick.modules.system.service.DictItemService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 字典项
 */
@RestController
@RequestMapping("/api/dicts/items")
@Api(tags = "系统：字典项管理", value = "字典项管理相关接口")
public class DictItemController extends BaseController {

    @Autowired
    private DictItemService dictItemService; // 字典项接口

    /**
     * 新增字典项
     */
    @PostMapping
    @ApiOperation(value = "新增字典项", notes = "新增字典项")
    public ResponseVo add(@ApiParam(name = "字典项实体", value = "参数体", required = true) @RequestBody SysDictItem dictItem) throws Exception {
        dictItemService.addDictItem(dictItem);
        ResponseVo responseVo = new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        return responseVo;
    }

    /**
     * 删除字典项，逻辑，支持批量
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除字典项", notes = "删除字典项，逻辑，支持批量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "字典项ID 如：1 或 1,2", dataType = "string", paramType = "path", required = true)
    })
    public ResponseVo delete(@PathVariable("id") String id) throws Exception {
        dictItemService.deleteDictItem(id);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 修改字典项
     */
    @PutMapping
    @ApiOperation(value = "修改字典项", notes = "修改字典项")
    public ResponseVo update(@ApiParam(name = "字典项实体", value = "参数体", required = true) @RequestBody SysDictItem dictItem) throws Exception {
        dictItemService.updateDictItem(dictItem);
        ResponseVo responseVo = new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        return responseVo;
    }

    /**
     * 获取 字典项列表
     */
    @GetMapping
    @ApiOperation(value = "获取字典项列表", notes = "支持分页，排序，条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页单位", dataType = "string", paramType = "query")
    })
    public VueTableResponse listDicts(@ApiIgnore VueTableRequest request) throws Exception {
        VueTableResponse response = dictItemService.findAll(request);
        return response;
    }

    /**
     * 获取 字典项详情
     */
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "获取字典项详情", notes = "获取字典项详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "字典项ID", dataType = "integer", paramType = "path", required = true)
    })
    public ResponseVo listDict(@PathVariable("id") Integer id) throws Exception {
        SysDictItem dictItem = dictItemService.selectByPk(id);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), dictItem);
    }
}
