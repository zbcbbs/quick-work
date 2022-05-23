package com.dongzz.quick.config;

import com.dongzz.quick.common.base.BaseMybatisMapper;
import com.dongzz.quick.common.plugin.mybatis.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import tk.mybatis.mapper.code.Style;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * Mybatis 核心配置
 */
@Configuration
public class MybatisConfig {

    @Value("${mybatis.mapper-locations}")
    private String mapperLocations; // mapper xml
    @Value("${mybatis.type-aliases-package}")
    private String typeAliasesPackage; // 别名
    @Value("${mybatis.configuration.mapUnderscoreToCamelCase}")
    private boolean mapUnderscoreToCamelCase; // 驼峰映射

    /**
     * 自定义扩展的 SqlSessionFactoryBean
     * 别名配置支持通配符 com.dongzz.quick.**.domain
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        // 配置
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(mapUnderscoreToCamelCase);
        sqlSessionFactoryBean.setConfiguration(configuration);

        // mapper xml
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources(mapperLocations);
            sqlSessionFactoryBean.setMapperLocations(resources);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 别名
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
//        sqlSessionFactoryBean.setPlugins(new Interceptor[]{}); // 设置插件 可以自己扩展一些插件
        return sqlSessionFactoryBean;
    }


    /**
     * 集成 TK 通用 Mapper 插件
     */
    @Configuration
    @AutoConfigureAfter(MybatisConfig.class) // 必须在MybatisConfig配置完成后才能加载，否则报错
    public static class TkMapperConfig {

        /**
         * 集成 TK 通用 Mapper插件
         */
        @Bean
        public MapperScannerConfigurer mapperScannerConfigurer() {
            Properties properties = new Properties();
            properties.setProperty("mappers", BaseMybatisMapper.class.getName());
            properties.setProperty("IDENTITY", "MYSQL"); // 数据库方言（取回主键的方式）
            properties.setProperty("notEmpty", "false"); // insert 和 update中，是否判断字符串类型 !=''，少数方法会用到
            properties.setProperty("style", Style.camelhump.name()); // 驼峰支持

            MapperScannerConfigurer scan = new MapperScannerConfigurer();
            scan.setSqlSessionFactoryBeanName("sqlSessionFactory"); // 多数据源时，必须配置
            scan.setBasePackage("com.dongzz.quick");// Mapper接口路径
            scan.setMarkerInterface(BaseMybatisMapper.class); // 直接继承了 BaseMybatisMapper 接口的才会被扫描，basePackage可以配置的范围更大
            scan.setProperties(properties);

            return scan;
        }
    }

}
