package com.dongzz.quick.common.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.dongzz.quick.common.config.bean.DruidProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * 阿里巴巴 Druid 相关配置
 */
@Configuration
public class DruidConfig {

    @Autowired
    private DruidProperties druidProperties;

    /**
     * 配置 Druid 数据源
     */
    @Bean
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        try {
            dataSource.setUrl(druidProperties.getUrl());
            dataSource.setDriverClassName(druidProperties.getDriverClassName());
            dataSource.setUsername(druidProperties.getUsername());
            dataSource.setPassword(druidProperties.getPassword());
            dataSource.setInitialSize(druidProperties.getInitialSize());
            dataSource.setMinIdle(druidProperties.getMinIdle());
            dataSource.setMaxActive(druidProperties.getMaxActive());
            dataSource.setMaxWait(druidProperties.getMaxWait());
            dataSource.setTimeBetweenEvictionRunsMillis(druidProperties.getTimeBetweenEvictionRunsMillis());
            dataSource.setMinEvictableIdleTimeMillis(druidProperties.getMinEvictableIdleTimeMillis());
            dataSource.setValidationQuery(druidProperties.getValidationQuery());
            dataSource.setTestWhileIdle(druidProperties.isTestWhileIdle());
            dataSource.setTestOnBorrow(druidProperties.isTestOnBorrow());
            dataSource.setTestOnReturn(druidProperties.isTestOnReturn());
            dataSource.setPoolPreparedStatements(druidProperties.isPoolPreparedStatements());
            dataSource.setMaxPoolPreparedStatementPerConnectionSize(druidProperties.getMaxPoolPreparedStatementPerConnectionSize());
            dataSource.setUseGlobalDataSourceStat(druidProperties.isUseGlobalDataSourceStat());
            dataSource.setFilters(druidProperties.getFilters()); // 扩展插件
            List<Filter> filters = new ArrayList<>(); // 监控慢SQL执行
            StatFilter statFilter = new StatFilter();
            statFilter.setLogSlowSql(druidProperties.isLogSlowSql());
            statFilter.setSlowSqlMillis(druidProperties.getSlowSqlMillis());
            filters.add(statFilter);
            dataSource.setProxyFilters(filters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    /**
     * 配置 Druid 监控平台 核心Servlet
     */
    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        StatViewServlet statViewServlet = new StatViewServlet(); // 创建
        servletRegistrationBean.setServlet(statViewServlet);
        servletRegistrationBean.addUrlMappings("/druid/*"); // 映射路径
        servletRegistrationBean.addInitParameter("allow", "127.0.0.1"); // IP白名单，多个ip可用逗号分隔
        // IP黑名单 当白名单和黑名单重复时，黑名单优先级更高
//        servletRegistrationBean.addInitParameter("deny", "127.0.0.1");
        // 控制台管理员 访问监控平台 需要输入账密
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "123456");
        servletRegistrationBean.addInitParameter("resetEnable", "false"); // 能否重置数据
        return servletRegistrationBean;
    }

    /**
     * 配置 Druid 监控平台 核心过滤器
     */
    @Bean
    public FilterRegistrationBean druidFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        WebStatFilter webStatFilter = new WebStatFilter(); // 创建
        filterRegistrationBean.setFilter(webStatFilter);
        filterRegistrationBean.addUrlPatterns("/*"); // 映射路径
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*"); // 资源放行
        return filterRegistrationBean;
    }

}
