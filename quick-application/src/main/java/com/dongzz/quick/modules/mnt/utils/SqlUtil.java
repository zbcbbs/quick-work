package com.dongzz.quick.modules.mnt.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.StringUtils;
import com.dongzz.quick.common.utils.CloseUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * sql 工具类
 */
@Slf4j
public class SqlUtil {

    /**
     * 获取数据源
     *
     * @param jdbcUrl
     * @param username
     * @param password
     * @return
     */
    private static DataSource getDataSource(String jdbcUrl, String username, String password) {
        DruidDataSource druidDataSource = new DruidDataSource();
        String className;
        try {
            className = DriverManager.getDriver(jdbcUrl.trim()).getClass().getName();
        } catch (SQLException e) {
            throw new RuntimeException("Get class name error: =" + jdbcUrl);
        }
        if (StringUtils.isEmpty(className)) {
            DataTypeEnum dataTypeEnum = DataTypeEnum.urlOf(jdbcUrl);
            if (null == dataTypeEnum) {
                throw new RuntimeException("Not supported data type: jdbcUrl=" + jdbcUrl);
            }
            druidDataSource.setDriverClassName(dataTypeEnum.getDriver());
        } else {
            druidDataSource.setDriverClassName(className);
        }

        druidDataSource.setUrl(jdbcUrl);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        // 配置获取连接等待超时的时间
        druidDataSource.setMaxWait(3000);
        // 配置初始化大小、最小、最大
        druidDataSource.setInitialSize(1);
        druidDataSource.setMinIdle(1);
        druidDataSource.setMaxActive(1);

        // 若连接出现异常则直接判定为失败而不是一直重试
        druidDataSource.setBreakAfterAcquireFailure(true);
        try {
            druidDataSource.init();
        } catch (SQLException e) {
            log.error("Exception during pool initialization", e);
            throw new RuntimeException(e.getMessage());
        }

        return druidDataSource;
    }

    /**
     * 获取连接
     *
     * @param jdbcUrl
     * @param username
     * @param password
     * @return
     */
    private static Connection getConnection(String jdbcUrl, String username, String password) {
        DataSource dataSource = getDataSource(jdbcUrl, username, password);
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (Exception ignored) {
        }
        try {
            int timeOut = 5;
            if (null == connection || connection.isClosed() || !connection.isValid(timeOut)) {
                log.info("connection is closed or invalid, retry get connection!");
                connection = dataSource.getConnection();
            }
        } catch (Exception e) {
            log.error("create connection error, jdbcUrl: {}", jdbcUrl);
            throw new RuntimeException("create connection error, jdbcUrl: " + jdbcUrl);
        }
        return connection;
    }

    /**
     * 关闭连接
     *
     * @param connection
     */
    private static void releaseConnection(Connection connection) {
        if (null != connection) {
            try {
                connection.close();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                log.error("connection close error：" + e.getMessage());
            }
        }
    }

    /**
     * 测试连接
     *
     * @param jdbcUrl
     * @param username
     * @param password
     * @return
     */
    public static boolean testConnection(String jdbcUrl, String username, String password) {
        Connection connection = null;
        try {
            connection = getConnection(jdbcUrl, username, password);
            if (null != connection) {
                return true;
            }
        } catch (Exception e) {
            log.info("Get connection failed:" + e.getMessage());
        } finally {
            releaseConnection(connection);
        }
        return false;
    }

    /**
     * 执行sql脚本
     *
     * @param jdbcUrl
     * @param username
     * @param password
     * @param script   脚本文件
     * @return
     */
    public static String execute(String jdbcUrl, String username, String password, File script) {
        Connection connection = null;
        try {
            List<String> sqls = readSqlScript(script);
            connection = getConnection(jdbcUrl, username, password);
            batchExecute(connection, sqls);
        } catch (Exception e) {
            log.error("sql脚本执行异常:{}", e.getMessage());
            return e.getMessage();
        } finally {
            releaseConnection(connection);
        }
        return "Ok";
    }


    /**
     * 批量执行sql语句
     *
     * @param connection
     * @param sqls
     */
    public static void batchExecute(Connection connection, List<String> sqls) {
        Statement st = null;
        try {
            st = connection.createStatement();
            for (String sql : sqls) {
                if (sql.endsWith(";")) {
                    sql = sql.substring(0, sql.length() - 1);
                }
                st.addBatch(sql);
            }
            st.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(st); // 释放资源
        }
    }

    /**
     * 解析脚本的中的sql语句,每个';'结尾为一条语句
     *
     * @param script /
     * @return /
     * @throws Exception e
     */
    private static List<String> readSqlScript(File script) throws Exception {
        List<String> sqls = Lists.newArrayList();
        StringBuilder sb = new StringBuilder();
        // try(){...} JDK 1.8 新语法 替换以前的 try catch finally
        // 括号中可以创建多个流对象，以';'分隔，所有流对象在语句结束后会自动关闭，释放资源
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(script), StandardCharsets.UTF_8))) {
            String tmp;
            while ((tmp = reader.readLine()) != null) {
                if (tmp.endsWith(";")) {
                    sb.append(tmp);
                    sqls.add(sb.toString());
                    sb.delete(0, sb.length());
                } else {
                    sb.append(tmp);
                }
            }
            if (!"".endsWith(sb.toString().trim())) {
                sqls.add(sb.toString());
            }
        }

        return sqls;
    }

}
