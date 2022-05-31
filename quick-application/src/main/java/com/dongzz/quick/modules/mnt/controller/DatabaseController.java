package com.dongzz.quick.modules.mnt.controller;

import com.dongzz.quick.common.base.BaseController;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.domain.ResponseVo;
import com.dongzz.quick.modules.mnt.domain.MntDatabase;
import com.dongzz.quick.modules.mnt.service.dto.DatabaseDto;
import com.dongzz.quick.modules.mnt.service.dto.DatabaseQueryCriteria;
import com.dongzz.quick.modules.mnt.service.DatabaseService;
import com.dongzz.quick.common.utils.Page;
import com.dongzz.quick.modules.mnt.utils.SqlUtil;
import com.dongzz.quick.tools.domain.ToolLocalFile;
import com.dongzz.quick.tools.service.FileLocalService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;

/**
 * 数据库管理
 *
 * @author zwk
 * @date 2022/05/19 15:55
 * @email zbcbbs@163.com
 */
@RestController
@RequestMapping("/api/database")
@Api(tags = "运维：数据库管理")
public class DatabaseController extends BaseController {

    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private FileLocalService fileService;

    /**
     * 添加
     */
    @PostMapping
    @ApiOperation(value = "添加", notes = "添加")
    public ResponseVo add(@ApiParam(name = "参数实体", value = "参数实体", required = true) @RequestBody DatabaseDto dto) throws Exception {
        databaseService.addDatabase(dto);
        ResponseVo responseVO = new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        return responseVO;
    }

    /**
     * 删除，支持批量
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除，支持批量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键ID 例如：1 或 1,2", dataType = "string", paramType = "path", required = true)
    })
    public ResponseVo delete(@PathVariable("id") String id) throws Exception {
        databaseService.deleteDatabase(id);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 修改
     */
    @PutMapping
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseVo update(@ApiParam(name = "参数实体", value = "参数实体", required = true) @RequestBody DatabaseDto dto) throws Exception {
        databaseService.updateDatabase(dto);
        ResponseVo responseVO = new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        return responseVO;
    }

    /**
     * 查询列表
     */
    @GetMapping
    @ApiOperation(value = "列表查询", notes = "支持分页，排序，条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "分页单位", dataType = "string", paramType = "query")
    })
    public VueTableResponse listDatabase(@ApiIgnore VueTableRequest request) throws Exception {
        VueTableResponse response = databaseService.findAll(request);
        return response;
    }

    /**
     * 查询列表
     */
    @GetMapping("/listDatabase")
    @ApiOperation(value = "列表查询", notes = "支持分页，排序，条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页单位", dataType = "string", paramType = "query")
    })
    public ResponseVo listDatabase(Page<MntDatabase> page, DatabaseQueryCriteria criteria) throws Exception {
        Page<MntDatabase> data = databaseService.findAll(page, criteria);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    /**
     * 查询详情
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询详情", notes = "查询详情")
    public ResponseVo listDatabase(@PathVariable("id") String id) throws Exception {
        MntDatabase data = databaseService.selectByPk(id);
        ResponseVo responseVO = new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
        return responseVO;
    }

    /**
     * 测试连接
     */
    @PostMapping("/checkConnection")
    @ApiOperation("测试连接")
    public ResponseVo checkConnection(@RequestBody DatabaseDto dto) throws Exception {
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), databaseService.checkConnection(dto));
    }

    /**
     * sql脚本上传执行
     */
    @PostMapping("/execute")
    @ApiOperation("sql脚本执行上传")
    public ResponseVo execute(MultipartFile file) throws Exception {
        // 查询要操作的数据库
        String id = getParameter("id");
        MntDatabase database = databaseService.selectByPk(id);
        ToolLocalFile tlf = fileService.addFile(file, "");
        // 上传成功 解析并执行
        if (null != tlf) {
            File script = new File(tlf.getCachePath());
            // 返回'Ok' 或 异常信息
            String result = SqlUtil.execute(database.getJdbcUrl(), database.getUsername(), database.getPassword(), script);
            // 清除上传的脚本
            fileService.deleteFile(tlf.getId());
            return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), result);
        }
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 导出数据
     */
    @GetMapping("/export")
    @ApiOperation("导出数据")
    public void export(@ApiIgnore VueTableRequest request) throws Exception {
        databaseService.download(request, response);
    }
}