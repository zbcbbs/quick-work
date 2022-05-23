package com.dongzz.quick.generator.controller;

import com.dongzz.quick.common.base.BaseController;
import com.dongzz.quick.common.domain.ResponseVo;
import com.dongzz.quick.generator.domain.CodeGenConfig;
import com.dongzz.quick.generator.service.GenConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 代码生成配置
 */
@RestController
@RequestMapping("/api/gen/configs")
@Api(tags = "代码：自动生成配置", value = "代码生成相关配置")
public class GenConfigController extends BaseController {

    @Autowired
    private GenConfigService configService;

    /**
     * 修改配置
     */
    @PutMapping
    @ApiOperation(value = "修改配置", notes = "修改配置")
    public ResponseVo update(@ApiParam(name = "配置实体", value = "参数体") @RequestBody CodeGenConfig config) throws Exception {
        configService.updateConfig(config);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 查询配置
     */
    @GetMapping("/{tableName}")
    @ApiOperation(value = "查询配置", notes = "查询配置")
    @ApiImplicitParam(name = "tableName", dataType = "string", paramType = "path", required = true)
    public ResponseVo listConfig(@PathVariable("tableName") String tableName) throws Exception {
        CodeGenConfig config = configService.findByTableName(tableName);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), config);
    }

}
