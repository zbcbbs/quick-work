package com.dongzz.quick.logging.controller;

import com.dongzz.quick.common.base.BaseController;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.domain.ResponseVo;
import com.dongzz.quick.logging.service.LogsService;
import com.dongzz.quick.logging.domain.SysLogs;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 日志
 */
@RestController
@RequestMapping("/api/logs")
@Api(tags = "日志：日志管理", value = "日志管理相关接口")
public class LogsController extends BaseController {

    @Autowired
    private LogsService logsService;

    /**
     * 删除日志，逻辑，支持批量
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除日志", notes = "删除日志，逻辑，支持批量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "日志ID 如：1 或 1,2", dataType = "string", paramType = "path", required = true)
    })
    public ResponseVo delete(@PathVariable("id") String id) throws Exception {
        logsService.deleteLogs(id);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 批量清空日志
     */
    @DeleteMapping("/deleteAll")
    @ApiOperation("批量清空日志")
    public ResponseVo delete(@ApiIgnore VueTableRequest request) throws Exception {
        logsService.deleteLogs(request);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 获取 日志列表
     */
    @GetMapping
    @ApiOperation(value = "获取日志列表", notes = "支持分页，排序，条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页单位", dataType = "string", paramType = "query")
    })
    public VueTableResponse listLogs(@ApiIgnore VueTableRequest request) throws Exception {
        VueTableResponse response = logsService.findAll(request);
        return response;
    }

    /**
     * 获取 日志详情
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取日志详情", notes = "获取日志详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "日志ID", dataType = "long", paramType = "path", required = true)
    })
    public ResponseVo listLog(@PathVariable("id") Long id) throws Exception {
        SysLogs log = logsService.selectByPk(id);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), log);
    }

    /**
     * 导出数据
     */
    @GetMapping("/export")
    @ApiOperation("导出数据")
    public void export(@ApiIgnore VueTableRequest request) throws Exception {
        logsService.download(request, response);
    }

}
