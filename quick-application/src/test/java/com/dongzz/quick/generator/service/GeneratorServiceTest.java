package com.dongzz.quick.generator.service;

import com.dongzz.quick.SpringBootJunit;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.generator.domain.vo.TableInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成
 */
public class GeneratorServiceTest extends SpringBootJunit {

    @Autowired
    GeneratorService generatorService;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    NamedParameterJdbcTemplate namedJdbcTemplate;

    @Test
    public void findAllTablesTest() throws Exception {
        VueTableRequest request = new VueTableRequest();
        request.setOffset(0);
        request.setLimit(4);
        Map<String, Object> params = new HashMap<>();
        params.put("name", "sys");
        params.put("orderBy", " order by create_time desc");
        request.setParams(params);
        VueTableResponse response = generatorService.findAllTables(request);
        List<TableInfo> data = (List<TableInfo>) response.getData();
        data.forEach((table) -> {
            System.out.println(table.getTableName());
        });
    }

}
