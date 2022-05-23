package com.dongzz.quick.tools.controller;

import com.dongzz.quick.common.base.BaseController;
import com.dongzz.quick.common.domain.ResponseVo;
import com.dongzz.quick.tools.service.ExcelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Excel操作
 */
@RestController
@RequestMapping("/api/excels")
@Api(tags = "工具：Excel操作", value = "Excel操作相关接口")
public class ExcelController extends BaseController {

    @Autowired
    private ExcelService excelService; // Excel服务接口

    /**
     * 校验sql，并返回查询数量
     */
    @PostMapping("/sql-count")
    @ApiOperation(value = "校验sql", notes = "校验sql")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sql", value = "sql 语句", dataType = "string", paramType = "query", required = true)
    })
    public ResponseVo checkSql(String sql) throws Exception {
        int count = excelService.checkSql(sql);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), count);
    }

    /**
     * 显示要导出的数据
     */
    @PostMapping("/show-datas")
    @ApiOperation(value = "显示导出的数据", notes = "显示导出的数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sql", value = "sql 语句", dataType = "string", paramType = "query", required = true)
    })
    public ResponseVo showData(String sql) throws Exception {
        List<Object[]> data = excelService.showSqlData(sql);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    /**
     * 导出excel 到浏览器
     */
    @PostMapping
    @ApiOperation(value = "导出Excel", notes = "导出Excel")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sql", value = "sql 语句", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "fileName", value = "excel 文件名", dataType = "string", paramType = "query", required = true)
    })
    public void downloadExcel(String sql, String fileName) throws Exception {
        excelService.downloadExcel(sql, fileName, response);
    }
}
