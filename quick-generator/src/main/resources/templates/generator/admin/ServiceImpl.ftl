package ${package}.service.impl;

import com.dongzz.quick.common.base.BaseMybatisServiceImpl;
import com.dongzz.quick.common.plugin.vuetables.VueTableHandler;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.utils.Util;
import ${package}.domain.${originalClassName};
import ${package}.service.dto.${className}Dto;
import ${package}.service.dto.${className}QueryCriteria;
import ${package}.dao.${originalClassName}Mapper;
import ${package}.service.${className}Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.bean.BeanUtil;
import com.dongzz.quick.common.utils.Page;

import java.util.*;

@Service
@Transactional
public class ${className}ServiceImpl extends BaseMybatisServiceImpl<${originalClassName}> implements ${className}Service {

    @Autowired
    private ${originalClassName}Mapper ${changeClassName}Mapper;

    @Override
    public void add${className}(${className}Dto dto) throws Exception {
        ${originalClassName} ${changeClassName} = new ${originalClassName}();
        BeanUtil.copyProperties(dto, ${changeClassName});
        ${changeClassName}Mapper.insertSelective(${changeClassName});
    }

    @Override
    public void delete${className}(String id) throws Exception {
        if (StringUtils.isNotBlank(id)) {
            if (id.contains(",")) {
                List<String> ids = Util.strSplitToList(id, ",");
                for (String did : ids) {
                    ${changeClassName}Mapper.deleteByPrimaryKey(${pkColumnType}.valueOf(did));
                }
            } else {
                ${changeClassName}Mapper.deleteByPrimaryKey(${pkColumnType}.valueOf(id));
            }
        }
    }

    @Override
    public void update${className}(${className}Dto dto) throws Exception {
        ${originalClassName} ${changeClassName} = new ${originalClassName}();
        BeanUtil.copyProperties(dto, ${changeClassName});
        ${changeClassName}Mapper.updateByPrimaryKeySelective(${changeClassName});
    }

    @Override
    public VueTableResponse findAll(VueTableRequest request) throws Exception {
        VueTableHandler handler = new VueTableHandler(new VueTableHandler.CountHandler() {

                @Override
                public int count(VueTableRequest request) {
                    try {
                        return ${changeClassName}Mapper.count(request.getParams());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            }, new VueTableHandler.ListHandler() {

                @Override
                public List<?> list(VueTableRequest request) {
                    try {
                        return ${changeClassName}Mapper.selectPage(request.getParams(), request.getOffset(), request.getLimit());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new ArrayList<>();
                }
            }
        });
        VueTableResponse response = handler.handle(request);
        return response;
    }

    @Override
    public Page<${originalClassName}> findAll(Page<${originalClassName}> page, ${className}QueryCriteria criteria) throws Exception {
        Page<${originalClassName}> result = selectPage(page, criteria);
        return result;
    }
}