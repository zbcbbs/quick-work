package com.dongzz.quick.tools.service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Excel 相关服务接口
 */
public interface ExcelService {

    /**
     * 校验sql，返回sql查询的数量
     *
     * @param sql sql语句
     * @return
     * @throws Exception
     */
    int checkSql(String sql) throws Exception;

    /**
     * 页面展示执行sql后得到的数据
     *
     * @param sql sql语句
     * @return
     * @throws Exception
     */
    List<Object[]> showSqlData(String sql) throws Exception;

    /**
     * 将执行sql得到的数据，导出到excel
     *
     * @param sql      sql语句
     * @param fileName 文件名
     * @param response
     * @throws Exception
     */
    void downloadExcel(String sql, String fileName, HttpServletResponse response) throws Exception;
}
