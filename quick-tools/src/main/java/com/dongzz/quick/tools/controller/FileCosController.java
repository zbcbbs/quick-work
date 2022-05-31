package com.dongzz.quick.tools.controller;

import com.dongzz.quick.common.base.BaseController;
import com.dongzz.quick.common.domain.ResponseVo;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.tools.domain.ToolCosConfig;
import com.dongzz.quick.tools.domain.ToolCosFile;
import com.dongzz.quick.tools.service.FileCosService;
import com.dongzz.quick.tools.utils.CosUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 腾讯云上传
 */
@RestController
@RequestMapping("/api/cos/files")
@Api(tags = "工具：腾讯云存储", value = "腾讯云上传相关接口")
public class FileCosController extends BaseController {

    @Autowired
    private FileCosService fileService;

    /**
     * 保存配置
     */
    @PutMapping("/config")
    @ApiOperation(value = "保存配置", notes = "保存配置")
    public ResponseVo config(@ApiParam(name = "配置实体", value = "配置实体", required = true) @RequestBody ToolCosConfig config) throws Exception {
        fileService.config(config);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 查询配置
     */
    @GetMapping("/config")
    @ApiOperation(value = "查询配置", notes = "查询配置")
    public ResponseVo find() throws Exception {
        ToolCosConfig config = fileService.find();
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), config);
    }

    /**
     * 腾讯云上传
     */
    @PostMapping
    @ApiOperation(value = "腾讯云上传", notes = "腾讯云上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "单个文件", dataType = "file", paramType = "body", required = true)
    })
    public ResponseVo upload(MultipartFile file) throws Exception {
        ToolCosConfig config = fileService.find();
        ToolCosFile cosFile = fileService.addFile(file, config);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), cosFile);
    }

    /**
     * 下载
     */
    @GetMapping("/download")
    @ApiOperation(value = "下载", notes = "下载")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", dataType = "string", paramType = "query", required = true)
    })
    public void download(String id) throws Exception {
        ToolCosConfig config = fileService.find();
        ToolCosFile file = fileService.selectByPk(id);
        CosUtil.download(config, file.getCachePath(), response);
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除文件", notes = "删除文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文件ID", dataType = "string", paramType = "path", required = true)
    })
    public ResponseVo delete(@PathVariable String id) throws Exception {
        ToolCosConfig config = fileService.find();
        fileService.deleteFile(id, config);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 修改
     */
    @PutMapping
    @ApiOperation(value = "修改文件", notes = "修改文件")
    public ResponseVo update(@ApiParam(name = "文件实体", value = "参数体", required = true) @RequestBody ToolCosFile file) throws Exception {
        fileService.updateFile(file);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 获取 文件列表
     */
    @GetMapping
    @ApiOperation(value = "获取文件列表", notes = "支持分页，排序，条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页单位", dataType = "string", paramType = "query")
    })
    public VueTableResponse listFiles(@ApiIgnore VueTableRequest request) throws Exception {
        VueTableResponse response = fileService.findAll(request);
        return response;
    }

    /**
     * 导出
     */
    @GetMapping("/export")
    @ApiOperation(value = "导出", notes = "导出")
    public void export(@ApiIgnore VueTableRequest request) throws Exception {
        VueTableResponse vtp = fileService.findAll(request);
        List<ToolCosFile> files = (List<ToolCosFile>) vtp.getData();
        fileService.download(files, response);
    }
}
