package com.dongzz.quick.generator.service;

import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.generator.domain.CodeColumnInfo;
import com.dongzz.quick.generator.domain.CodeGenConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 代码生成 相关服务接口
 */
public interface GeneratorService {

    /**
     * 修改表的字段配置
     *
     * @param columns 字段信息
     * @throws Exception
     */
    void updateColumn(List<CodeColumnInfo> columns) throws Exception;

    /**
     * 同步表信息
     *
     * @param oldColumns 已有的字段信息
     * @param nowColumns 当前的字段信息
     */
    void sync(List<CodeColumnInfo> oldColumns, List<CodeColumnInfo> nowColumns) throws Exception;

    /**
     * 代码生成
     *
     * @param config  配置信息
     * @param columns 字段信息
     */
    void generator(CodeGenConfig config, List<CodeColumnInfo> columns) throws Exception;

    /**
     * 预览
     *
     * @param config  配置信息
     * @param columns 字段信息
     * @return /
     */
    List<Map<String, Object>> preview(CodeGenConfig config, List<CodeColumnInfo> columns) throws Exception;

    /**
     * 打包下载
     *
     * @param config   配置信息
     * @param columns  字段信息
     * @param response /
     */
    void download(CodeGenConfig config, List<CodeColumnInfo> columns, HttpServletResponse response) throws Exception;


    /**
     * 查询当前库的表信息
     *
     * @param request 条件
     * @return
     * @throws Exception
     */
    VueTableResponse findAllTables(VueTableRequest request) throws Exception;


    /**
     * 查询指定表的字段信息
     *
     * @param request 条件
     * @return
     * @throws Exception
     */
    VueTableResponse findAllColumns(VueTableRequest request) throws Exception;

    /**
     * 查询指定表的字段信息
     *
     * @param table 表名
     * @return
     * @throws Exception
     */
    List<CodeColumnInfo> findAllColumns(String table) throws Exception;
}
