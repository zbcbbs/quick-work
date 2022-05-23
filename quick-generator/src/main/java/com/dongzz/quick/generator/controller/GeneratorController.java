package com.dongzz.quick.generator.controller;

import com.dongzz.quick.common.base.BaseController;
import com.dongzz.quick.common.domain.ResponseVo;
import com.dongzz.quick.common.exception.ServiceException;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.generator.domain.CodeColumnInfo;
import com.dongzz.quick.generator.domain.CodeGenConfig;
import com.dongzz.quick.generator.service.GenConfigService;
import com.dongzz.quick.generator.service.GeneratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成
 */
@RestController
@RequestMapping("/api/generator")
@Api(tags = "代码：代码自动生成", value = "代码自动生成")
public class GeneratorController extends BaseController {

    @Autowired
    private GeneratorService generatorService;
    @Autowired
    private GenConfigService configService;
    @Value("${generator.enabled}")
    private Boolean generatorEnabled;

    /**
     * 修改表的字段配置
     */
    @PutMapping
    @ApiOperation("修改表的字段配置")
    public ResponseVo update(@RequestBody List<CodeColumnInfo> columns) throws Exception {
        generatorService.updateColumn(columns);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 查询当前库的表信息
     */
    @GetMapping("/tables")
    @ApiOperation("查询当前库的表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "string", paramType = "query")
    })
    public VueTableResponse listTables(VueTableRequest request) throws Exception {
        VueTableResponse response = generatorService.findAllTables(request);
        return response;
    }

    /**
     * 查询指定表的字段信息
     */
    @GetMapping("/columns")
    @ApiOperation("查询指定表的字段信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "tableName", dataType = "string", paramType = "query")
    })
    public VueTableResponse listColumns(VueTableRequest request) throws Exception {
        VueTableResponse response = generatorService.findAllColumns(request);
        return response;
    }

    /**
     * 同步表的字段信息
     */
    @PostMapping("/sync")
    @ApiOperation("同步表的字段信息")
    public ResponseVo sync(@RequestBody List<String> tables) throws Exception {
        for (String table : tables) {
            // 已经存储的字段信息
            VueTableRequest query = new VueTableRequest();
            Map<String, Object> params = new HashMap<>();
            params.put("tableName", table);
            query.setParams(params);
            List<CodeColumnInfo> oldColumns = (List<CodeColumnInfo>) generatorService.findAllColumns(query).getData();
            // 当前实时的字段信息
            List<CodeColumnInfo> nowColumns = generatorService.findAllColumns(table);
            // 同步刷新
            generatorService.sync(oldColumns, nowColumns);
        }
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 生成代码
     */
    @PostMapping("/{tableName}/{type}")
    @ApiOperation(value = "生成代码", notes = "生成代码")
    public ResponseVo generate(@PathVariable("tableName") String tableName, @PathVariable("type") Integer type) throws Exception {
        if (!generatorEnabled && type == 0) {
            throw new ServiceException("当前环境不允许生成代码，请选择预览或下载查看！");
        }
        VueTableRequest vtr = new VueTableRequest();
        Map<String, Object> params = new HashMap<>();
        params.put("tableName", tableName);
        vtr.setParams(params);
        CodeGenConfig config = configService.findByTableName(tableName); // 生成配置
        VueTableResponse result = generatorService.findAllColumns(vtr);
        List<CodeColumnInfo> columns = (List<CodeColumnInfo>) result.getData(); // 字段信息
        switch (type) {
            case 0: // 代码生成
                generatorService.generator(config, columns);
                break;
            case 1: // 预览
                List<Map<String, Object>> data = generatorService.preview(config, columns);
                return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
        }
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 下载代码
     */
    @PostMapping("/download/{tableName}")
    @ApiOperation(value = "下载代码", notes = "下载代码")
    public void download(@PathVariable("tableName") String tableName) throws Exception {
        VueTableRequest vtr = new VueTableRequest();
        Map<String, Object> params = new HashMap<>();
        params.put("tableName", tableName);
        vtr.setParams(params);
        CodeGenConfig config = configService.findByTableName(tableName); // 生成配置
        VueTableResponse result = generatorService.findAllColumns(vtr);
        List<CodeColumnInfo> columns = (List<CodeColumnInfo>) result.getData(); // 字段信息
        generatorService.download(config, columns, response);
    }
}
