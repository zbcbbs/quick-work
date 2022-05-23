package com.dongzz.quick.generator.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ZipUtil;
import com.dongzz.quick.common.exception.ServiceException;
import com.dongzz.quick.common.plugin.vuetables.VueTableHandler;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.utils.FileUtil;
import com.dongzz.quick.common.utils.StringUtil;
import com.dongzz.quick.generator.dao.ColumnInfoMapper;
import com.dongzz.quick.generator.domain.CodeColumnInfo;
import com.dongzz.quick.generator.domain.CodeGenConfig;
import com.dongzz.quick.generator.domain.vo.TableInfo;
import com.dongzz.quick.generator.service.GeneratorService;
import com.dongzz.quick.generator.utils.GenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GeneratorServiceImpl implements GeneratorService {

    public static final Logger logger = LoggerFactory.getLogger(GeneratorServiceImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ColumnInfoMapper columnMapper;

    @Override
    @Transactional
    public void updateColumn(List<CodeColumnInfo> columns) throws Exception {
        if (CollectionUtil.isNotEmpty(columns)) {
            columns.forEach((column) -> {
                columnMapper.updateByPrimaryKeySelective(column);
            });
        }
    }

    @Override
    public void sync(List<CodeColumnInfo> oldColumns, List<CodeColumnInfo> nowColumns) throws Exception {
        // 新增或修改字段信息
        for (CodeColumnInfo column : nowColumns) {
            List<CodeColumnInfo> columns = oldColumns.stream().filter(oc -> oc.getColumnName().equals(column.getColumnName())).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(columns)) {
                CodeColumnInfo oc = columns.get(0);
                oc.setColumnType(column.getColumnType());
                oc.setExtra(column.getExtra());
                oc.setKeyType(column.getKeyType());
                if (StringUtil.isBlank(oc.getRemark())) {
                    oc.setRemark(column.getRemark());
                }
                columnMapper.updateByPrimaryKeySelective(oc);
            } else {
                columnMapper.insertSelective(column);
            }
        }
        // 删除字段信息
        for (CodeColumnInfo column : oldColumns) {
            List<CodeColumnInfo> columns = nowColumns.stream().filter(nc -> nc.getColumnName().equals(column.getColumnName())).collect(Collectors.toList());
            if (CollectionUtil.isEmpty(columns)) {
                columnMapper.delete(column);
            }
        }

    }

    @Override
    public void generator(CodeGenConfig config, List<CodeColumnInfo> columns) throws Exception {
        if (config.getId() == null) {
            throw new ServiceException("请先配置生成器");
        }
        try {
            GenUtil.generator(columns, config);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException("生成失败，请手动处理生成的代码");
        }

    }

    @Override
    public List<Map<String, Object>> preview(CodeGenConfig config, List<CodeColumnInfo> columns) throws Exception {
        if (config.getId() == null) {
            throw new ServiceException("请先配置生成器");
        }
        List<Map<String, Object>> data = GenUtil.preview(columns, config);
        return data;
    }

    @Override
    public void download(CodeGenConfig config, List<CodeColumnInfo> columns, HttpServletResponse response) throws Exception {
        if (config.getId() == null) {
            throw new ServiceException("请先配置生成器");
        }
        try {
            File file = new File(GenUtil.download(columns, config));
            String zipPath = file.getPath() + ".zip";
            ZipUtil.zip(file.getPath(), zipPath); // zip压缩
            FileUtil.download(zipPath, FileUtil.getFileNameNoEx(zipPath), response);
        } catch (Exception e) {
            throw new ServiceException("打包下载失败");
        }
    }

    @Override
    public VueTableResponse findAllTables(VueTableRequest request) throws Exception {

        VueTableHandler handler = new VueTableHandler(new VueTableHandler.CountHandler() {

            @Override
            public int count(VueTableRequest request) {
                try {
                    Map<String, Object> params = request.getParams();
                    List<Object> args = new ArrayList<>();
                    // sql
                    StringBuilder sql = new StringBuilder("select count(*) from information_schema.tables where table_schema = (select database())");
                    if (ObjectUtil.isNotEmpty(params.get("name"))) {
                        sql.append(" and table_name like ?");
                        args.add("%" + params.get("name") + "%");
                    }

                    return jdbcTemplate.queryForObject(sql.toString(), Integer.class, args.toArray());
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        }, new VueTableHandler.ListHandler() {

            @Override
            public List<?> list(VueTableRequest request) {
                try {
                    Map<String, Object> params = request.getParams();
                    List<Object> args = new ArrayList<>();
                    // sql
                    StringBuilder sql = new StringBuilder("select table_name ,create_time , engine, table_collation, table_comment from information_schema.tables");
                    sql.append(" where table_schema = (select database())");
                    if (ObjectUtil.isNotEmpty(params.get("name"))) {
                        sql.append(" and table_name like ?");
                        args.add("%" + params.get("name") + "%");
                    }
                    if (ObjectUtil.isNotEmpty(params.get("orderBy"))) {
                        sql.append(params.get("orderBy"));
                    }
                    if (request.getLimit() != null && request.getLimit() != 0) {
                        sql.append(" limit ").append(request.getOffset()).append(",").append(request.getLimit());
                    }
                    List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), args.toArray());
                    List<TableInfo> tables = new ArrayList<>();
                    list.forEach((row) -> {
                        TableInfo table = new TableInfo();
                        table.setTableName(row.get("table_name"));
                        table.setEngine(row.get("engine"));
                        table.setCoding(row.get("table_collation"));
                        table.setRemark(row.get("table_comment"));
                        table.setCreateTime(row.get("create_time"));
                        tables.add(table);
                    });
                    return tables;
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
    public VueTableResponse findAllColumns(VueTableRequest request) throws Exception {
        List<CodeColumnInfo> columns;
        Integer count = columnMapper.count(request.getParams());
        if (count > 0) {
            columns = columnMapper.selectAllColumns(request.getParams(), request.getOffset(), request.getLimit());
        } else {
            columns = findAllColumns((String) request.getParams().get("tableName")); // 查询库中表字段信息
            int r = columnMapper.insertList(columns);// 存储字段信息
            if (r > 0) {
                columns = columnMapper.selectAllColumns(request.getParams(), request.getOffset(), request.getLimit());
            }
            count = columns.size();
        }

        return new VueTableResponse(count, count, columns);
    }

    @Override
    public List<CodeColumnInfo> findAllColumns(String table) throws Exception {
        // sql
        String sql = "select `column_name`, is_nullable, data_type, column_comment, column_key, extra from information_schema.columns " +
                "where `table_name` = ? and table_schema = (select database()) order by ordinal_position";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, table);
        List<CodeColumnInfo> columns = new ArrayList<>();
        list.forEach((row) -> {
            CodeColumnInfo column = new CodeColumnInfo();
            column.setTableName(table);
            column.setColumnName((String) row.get("column_name"));
            column.setNotNull("NO".equals(row.get("is_nullable")));
            column.setColumnType((String) row.get("data_type"));
            column.setRemark((String) row.get("column_comment"));
            column.setKeyType((String) row.get("column_key"));
            column.setExtra((String) row.get("extra"));
            // 特别处理
            if (GenUtil.PK.equalsIgnoreCase(column.getKeyType()) && GenUtil.EXTRA.equalsIgnoreCase(column.getExtra())) {
                column.setNotNull(false);
            }
            column.setListShow(true);
            column.setFormShow(true);
            columns.add(column);
        });
        return columns;
    }
}
