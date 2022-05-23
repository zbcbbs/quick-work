package com.dongzz.quick.tools.service.impl;

import com.dongzz.quick.common.exception.ServiceException;
import com.dongzz.quick.common.utils.ExcelUtil;
import com.dongzz.quick.tools.service.ExcelService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private JdbcTemplate jdbcTemplate; // JdbcTemplate

    @Override
    public int checkSql(String sql) throws Exception {
        getAndCheckSql(sql); // 检查sql语法
        Integer count = 0;
        try {
            count = jdbcTemplate.queryForObject("select count(1) from (" + sql + ") t", Integer.class);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        return count;
    }

    @Override
    public List<Object[]> showSqlData(String sql) throws Exception {
        // 校验sql
        sql = getAndCheckSql(sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

        if (!CollectionUtils.isEmpty(list)) {
            Map<String, Object> map = list.get(0);

            String[] headers = new String[map.size()];
            int i = 0;
            for (String key : map.keySet()) {
                headers[i++] = key;
            }

            List<Object[]> datas = new ArrayList<>(list.size());
            datas.add(headers);
            for (Map<String, Object> m : list) {
                Object[] objects = new Object[headers.length];
                for (int j = 0; j < headers.length; j++) {
                    objects[j] = m.get(headers[j]);
                }

                datas.add(objects);
            }

            return datas;
        }

        return Collections.emptyList();
    }

    /**
     * 校验并获取sql
     *
     * @param sql sql 语句
     * @return
     */
    private String getAndCheckSql(String sql) {
        sql = sql.trim().toLowerCase();
        if (sql.endsWith(";") || sql.endsWith("；")) {
            sql = sql.substring(0, sql.length() - 1);
        }
        if (!sql.startsWith("select")) {
            throw new ServiceException("仅支持select语句");
        }
        return sql;
    }

    @Override
    public void downloadExcel(String sql, String fileName, HttpServletResponse response) throws Exception {
        sql = getAndCheckSql(sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

        if (!CollectionUtils.isEmpty(list)) {
            Map<String, Object> map = list.get(0);

            String[] headers = new String[map.size()];
            int i = 0;
            for (String key : map.keySet()) {
                headers[i++] = key;
            }

            List<Object[]> datas = new ArrayList<>(list.size());
            for (Map<String, Object> m : list) {
                Object[] objects = new Object[headers.length];
                for (int j = 0; j < headers.length; j++) {
                    objects[j] = m.get(headers[j]);
                }

                datas.add(objects);
            }
            // 导出到浏览器
            ExcelUtil.writeExcel(StringUtils.isBlank(fileName) ? DigestUtils.md5Hex(sql) : fileName, headers, datas, response);
        }

    }
}
