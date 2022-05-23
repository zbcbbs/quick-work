package com.dongzz.quick.modules.quartz.controller;

import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.logging.annotation.Log;
import com.dongzz.quick.common.base.BaseController;
import com.dongzz.quick.common.domain.ResponseVo;
import com.dongzz.quick.modules.quartz.domain.SysQuartzJob;
import com.dongzz.quick.modules.quartz.service.QuartzJobService;
import com.dongzz.quick.logging.service.QuartzLogService;
import io.swagger.annotations.*;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Set;

/**
 * 定时任务
 */
@RestController
@RequestMapping("/api/jobs")
@Api(tags = "系统：定时任务管理", value = "定时任务管理相关接口")
public class QuartzJobController extends BaseController {

    @Autowired
    private QuartzJobService jobService;
    @Autowired
    private QuartzLogService logService;

    /**
     * 新增
     */
    @PostMapping
    @Log(module = "定时任务", operate = "新增")
    @ApiOperation(value = "新增任务", notes = "新增任务")
    public ResponseVo add(@ApiParam(name = "任务实体", value = "参数体", required = true) @RequestBody SysQuartzJob quartzJob) throws Exception {
        jobService.addQuartzJob(quartzJob);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 删除，支持批量
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除任务", notes = "删除任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "任务ID", dataType = "string", paramType = "path", required = true)
    })
    public ResponseVo delete(@PathVariable String id) throws Exception {
        jobService.deleteQuartzJob(id);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 修改
     */
    @PutMapping
    @ApiOperation(value = "修改任务", notes = "修改任务")
    public ResponseVo update(@ApiParam(name = "任务实体", value = "参数体", required = true) @RequestBody SysQuartzJob quartzJob) throws Exception {
        jobService.updateQuartzJob(quartzJob);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 修改任务状态
     */
    @PutMapping("/{id}")
    @ApiOperation("修改任务状态")
    public ResponseVo update(@PathVariable("id") Integer id) throws Exception {
        SysQuartzJob quartzJob = jobService.selectByPk(id);
        jobService.updateStatus(quartzJob);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 执行
     */
    @PutMapping("/exec/{id}")
    @ApiOperation("执行任务")
    public ResponseVo execute(@PathVariable("id") Integer id) throws Exception {
        SysQuartzJob quartzJob = jobService.selectByPk(id);
        jobService.executeQuartzJob(quartzJob);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 获取 定时任务列表
     */
    @GetMapping
    @ApiOperation(value = "获取定时任务列表", notes = "支持分页，排序，条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页单位", dataType = "string", paramType = "query")
    })
    public VueTableResponse listQuartzJobs(@ApiIgnore VueTableRequest request) throws Exception {
        VueTableResponse response = jobService.findAll(request);
        return response;
    }

    /**
     * 获取 定时任务详情
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取任务详情", notes = "获取任务详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "任务ID", dataType = "integer", paramType = "path", required = true)
    })
    public ResponseVo listQuartzJob(@PathVariable Integer id) throws Exception {
        SysQuartzJob quartzJob = jobService.selectByPk(id);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), quartzJob);
    }

    /**
     * 获取 spring bean 名称列表
     */
    @GetMapping("/beans")
    @ApiOperation(value = "获取 spring bean 名称列表", notes = "获取 spring bean 名称列表")
    public ResponseVo listSpringBeanNames() throws Exception {
        List<String> data = jobService.getSpringBeanNames();
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    /**
     * 获取 spring bean 无参方法名称列表
     */
    @GetMapping("/beans/{name}")
    @ApiOperation(value = "获取 spring bean 无参方法名称列表", notes = "获取 spring bean 无参方法名称列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "spring bean 名称", dataType = "string", paramType = "path", required = true)
    })
    public ResponseVo listSpringBeanMethodNames(@PathVariable String name) throws Exception {
        Set<String> data = jobService.getBeanMethodNames(name);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    /**
     * 校验 任务表达式是否合法
     */
    @GetMapping("/cron")
    @ApiOperation(value = "检验 cron 表达式", notes = "检验 cron 表达式")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cron", value = "cron 表达式", dataType = "string", paramType = "query", required = true)
    })
    public ResponseVo checkCron(String cron) throws Exception {
        boolean data = CronExpression.isValidExpression(cron);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    // ------------------------------------- 任务日志 ----------------------------------------

    /**
     * 获取任务日志列表
     */
    @GetMapping("/logs")
    @ApiOperation("获取任务日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页单位", dataType = "string", paramType = "query")
    })
    public VueTableResponse listQuartzLogs(@ApiIgnore VueTableRequest request) throws Exception {
        VueTableResponse response = logService.findAll(request);
        return response;
    }
}
