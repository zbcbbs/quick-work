package com.dongzz.quick.modules.system.controller;

import com.dongzz.quick.common.base.BaseController;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.domain.ResponseVo;
import com.dongzz.quick.modules.system.domain.SysDict;
import com.dongzz.quick.modules.system.service.DictService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 字典
 */
@RestController
@RequestMapping("/api/dicts")
@Api(tags = "系统：字典管理", value = "字典管理相关接口")
public class DictController extends BaseController {

    @Autowired
    private DictService dictService; // 字典接口

    /**
     * 新增字典
     */
    @PostMapping
    @ApiOperation(value = "新增字典", notes = "新增字典")
    public ResponseVo add(@ApiParam(name = "字典实体", value = "参数体", required = true) @RequestBody SysDict dict) throws Exception {
        dictService.addDict(dict);
        ResponseVo responseVO = new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        return responseVO;
    }

    /**
     * 删除字典，逻辑，支持批量
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除字典", notes = "删除字典，逻辑，支持批量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "字典ID 如：1 或 1,2", dataType = "string", paramType = "path", required = true)
    })
    public ResponseVo delete(@PathVariable("id") String id) throws Exception {
        dictService.deleteDict(id);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 修改字典
     */
    @PutMapping
    @ApiOperation(value = "修改字典", notes = "修改字典")
    public ResponseVo update(@ApiParam(name = "字典实体", value = "参数体", required = true) @RequestBody SysDict dict) throws Exception {
        dictService.updateDict(dict);
        ResponseVo responseVO = new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        return responseVO;
    }

    /**
     * 获取 字典列表
     */
    @GetMapping
    @ApiOperation(value = "获取字典列表", notes = "支持分页，排序，条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页单位", dataType = "string", paramType = "query")
    })
    public VueTableResponse listDicts(@ApiIgnore VueTableRequest request) throws Exception {
        VueTableResponse response = dictService.findAll(request);
        return response;
    }

    /**
     * 获取 字典列表
     */
    @GetMapping("/all")
    @ApiOperation(value = "获取字典列表")
    public ResponseVo listDicts() throws Exception {
        List<SysDict> dicts = dictService.findAll();
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), dicts);
    }

    /**
     * 获取 字典详情
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取字典详情", notes = "获取字典详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "字典ID", dataType = "integer", paramType = "path", required = true)
    })
    public ResponseVo listDict(@PathVariable("id") Integer id) throws Exception {
        SysDict dict = dictService.selectByPk(id);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), dict);
    }

}
