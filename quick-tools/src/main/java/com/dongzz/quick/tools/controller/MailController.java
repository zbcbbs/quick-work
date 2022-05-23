package com.dongzz.quick.tools.controller;

import com.dongzz.quick.common.base.BaseController;
import com.dongzz.quick.common.domain.ResponseVo;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.tools.domain.ToolMail;
import com.dongzz.quick.tools.domain.ToolMailConfig;
import com.dongzz.quick.tools.service.MailService;
import com.dongzz.quick.tools.service.dto.MailDto;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 邮件
 */
@RestController
@RequestMapping("/api/mails")
@Api(tags = "工具：邮件管理", value = "邮件管理相关接口")
public class MailController extends BaseController {

    @Autowired
    private MailService mailService;

    /**
     * 保存配置
     */
    @PutMapping("/config")
    @ApiOperation(value = "保存配置", notes = "保存配置")
    public ResponseVo config(@ApiParam(name = "配置实体", value = "参数体", required = true) @RequestBody ToolMailConfig config) throws Exception {
        mailService.config(config);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 查询配置
     */
    @GetMapping("/getConfig")
    @ApiOperation(value = "查询配置", notes = "查询配置")
    public ResponseVo find() throws Exception {
        ToolMailConfig config = mailService.find();
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), config);
    }

    /**
     * 新增并发送邮件
     */
    @PostMapping("/send")
    @ApiOperation(value = "新增并发送邮件", notes = "新增并发送邮件")
    public ResponseVo send(@ApiParam(name = "邮件实体", value = "参数体", required = true) @RequestBody MailDto mailDto) throws Exception {
        ToolMailConfig config = mailService.find(); // 读取配置
        mailService.addMail(mailDto, config);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 新增邮件
     */
    @PostMapping
    @ApiOperation(value = "新增邮件", notes = "新增邮件")
    public ResponseVo add(@ApiParam(name = "邮件实体", value = "参数体", required = true) @RequestBody MailDto mailDto) throws Exception {
        mailService.addMail(mailDto);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 获取 邮件列表
     */
    @GetMapping
    @ApiOperation(value = "获取邮件列表", notes = "支持分页，排序，条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页单位", dataType = "string", paramType = "query")
    })
    public VueTableResponse listMails(@ApiIgnore VueTableRequest request) throws Exception {
        VueTableResponse response = mailService.findAll(request);
        return response;
    }

    /**
     * 获取 邮件详情
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取邮件详情", notes = "获取邮件详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "邮件ID", dataType = "integer", paramType = "path", required = true)
    })
    public ResponseVo listMail(@PathVariable Integer id) throws Exception {
        ToolMail mail = mailService.selectByPk(id); // 邮件详情
        List<Map<String, Object>> mailDetailsTo = mailService.findMailDetailsTo(id); // 发送详情
        Map<String, Object> data = new HashMap<>();
        data.put("mail", mail);
        data.put("mailDetail", mailDetailsTo);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }
}
