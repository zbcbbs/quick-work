package ${package}.service;

import com.dongzz.quick.common.base.BaseMybatisService;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import ${package}.domain.${originalClassName};
import ${package}.service.dto.${className}Dto;
import ${package}.service.dto.${className}QueryCriteria;
import com.dongzz.quick.common.utils.Page;

/**
 * ${comment}
 * @author ${author}
 * @date ${date}
 * @email ${email}
 */
public interface ${className}Service extends BaseMybatisService<${originalClassName}> {

    /**
     * 添加
     *
     * @param dto /
     * @throws Exception
     */
    void add${className}(${className}Dto dto) throws Exception;

    /**
     * 删除，支持批量
     *
     * @param id 主键ID 1 或 1,2
     * @throws Exception
     */
    void delete${className}(String id) throws Exception;

    /**
     * 修改
     *
     * @param dto /
     * @throws Exception
     */
    void update${className}(${className}Dto dto) throws Exception;

    /**
     * 分页条件查询
     *
     * @param request 分页请求
     * @return
     * @throws Exception
     */
    VueTableResponse findAll(VueTableRequest request) throws Exception;

    /**
     * 分页条件查询
     *
     * @param page 分页参数
     * @param criteria 条件
     * @return
     * @throws Exception
     */
    Page<${originalClassName}> findAll(Page<${originalClassName}> page, ${className}QueryCriteria criteria) throws Exception;
}