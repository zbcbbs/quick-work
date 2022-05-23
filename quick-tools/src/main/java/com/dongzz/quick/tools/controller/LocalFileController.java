package com.dongzz.quick.tools.controller;

import com.dongzz.quick.common.base.BaseController;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.utils.FileUtil;
import com.dongzz.quick.common.domain.ResponseVo;
import com.dongzz.quick.tools.domain.ToolLocalFile;
import com.dongzz.quick.tools.service.LocalFileService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 本地上传
 */
@RestController
@RequestMapping("/api/local/files")
@Api(tags = "工具：本地存储", value = "本地上传相关接口")
public class LocalFileController extends BaseController {

    @Autowired
    private LocalFileService fileService; // 本地上传

    /**
     * 本地上传
     */
    @PostMapping
    @ApiOperation(value = "本地上传", notes = "本地上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "单个文件", dataType = "file", paramType = "body", required = true)
    })
    public ResponseVo upload(@RequestParam("file") MultipartFile file, @RequestParam("name") String name) throws Exception {
        ToolLocalFile localFile = fileService.addFile(file, name);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), localFile);
    }

    /**
     * 文件下载
     */
    @GetMapping("/download")
    @ApiOperation(value = "下载", notes = "下载")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "存储路径", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "fileName", value = "文件名", dataType = "string", paramType = "query", required = true)
    })
    public void download(String path, String fileName) throws Exception {
        FileUtil.download(path, fileName, response);
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除文件", notes = "删除文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文件ID", dataType = "string", paramType = "path", required = true)
    })
    public ResponseVo delete(@PathVariable String id) throws Exception {
        fileService.deleteFile(id);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 修改文件
     */
    @PutMapping
    @ApiOperation(value = "修改文件", notes = "修改文件")
    public ResponseVo update(@ApiParam(name = "文件实体", value = "参数体", required = true) @RequestBody ToolLocalFile file) throws Exception {
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
     * 导出 Excel
     */
    @GetMapping("/export")
    @ApiOperation(value = "导出", notes = "导出")
    public void export(@ApiIgnore VueTableRequest request) throws Exception {
        VueTableResponse vtp = fileService.findAll(request);
        List<ToolLocalFile> files = (List<ToolLocalFile>) vtp.getData();
        fileService.download(files, response);
    }
}
