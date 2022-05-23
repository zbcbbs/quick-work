package ${package}.controller;

import com.dongzz.quick.common.base.BaseController;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.domain.ResponseVo;
import ${package}.domain.${originalClassName};
import ${package}.service.dto.${className}Dto;
import ${package}.service.dto.${className}QueryCriteria;
import ${package}.service.${className}Service;
import com.dongzz.quick.common.utils.Page;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * ${comment}
 * @author ${author}
 * @date ${date}
 * @email ${email}
 */
@RestController
@RequestMapping("/api/${changeClassName}")
@Api(tags = "${apiAlias}")
public class ${className}Controller extends BaseController {

    @Autowired
    private ${className}Service ${changeClassName}Service;

    /**
     * 添加
     */
    @PostMapping
    @ApiOperation(value = "添加", notes = "添加")
    public ResponseVo add(@ApiParam(name = "参数实体", value = "参数实体", required = true) @RequestBody ${className}Dto dto) throws Exception {
        ${changeClassName}Service.add${className}(dto);
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
        ${changeClassName}Service.delete${className}(id);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
    }

    /**
     * 修改
     */
    @PutMapping
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseVo update(@ApiParam(name = "参数实体", value = "参数实体", required = true) @RequestBody ${className}Dto dto) throws Exception {
        ${changeClassName}Service.update${className}(dto);
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
    public VueTableResponse list${className}(@ApiIgnore VueTableRequest request) throws Exception {
        VueTableResponse response = ${changeClassName}Service.findAll(request);
        return response;
    }

    /**
     * 查询列表
     */
    @GetMapping("/list${className}")
    @ApiOperation(value = "列表查询", notes = "支持分页，排序，条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页码", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页单位", dataType = "string", paramType = "query")
    })
    public ResponseVo list${className}(Page<${originalClassName}> page, ${className}QueryCriteria criteria) throws Exception {
        Page<${originalClassName}> data = ${changeClassName}Service.findAll(page, criteria);
        return new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    /**
     * 查询详情
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询详情", notes = "查询详情")
    public ResponseVo list${className}(@PathVariable("id") ${pkColumnType} id) throws Exception {
        ${originalClassName} data = ${changeClassName}Service.selectByPk(id);
        ResponseVo responseVO = new ResponseVo(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
        return responseVO;
    }
}