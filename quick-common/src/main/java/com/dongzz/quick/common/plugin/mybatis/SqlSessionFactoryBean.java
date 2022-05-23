package com.dongzz.quick.common.plugin.mybatis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义 SqlSessionFactoryBean
 */
public class SqlSessionFactoryBean extends org.mybatis.spring.SqlSessionFactoryBean {

    public static final Logger logger = LoggerFactory.getLogger(SqlSessionFactoryBean.class);

    public static final String DEFAULT_RESOURCE_PATTERN = "**/*.class"; // 别名包通配符

    /**
     * 重写 实体别名扩展 支持通配符
     *
     * @param typeAliasesPackage 格式： com.dongzz.quick.**.domain
     */
    @Override
    public void setTypeAliasesPackage(String typeAliasesPackage) {
        // 资源路径解析器
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 元数据读取
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);
        // 解析路径
        typeAliasesPackage = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(typeAliasesPackage) + "/" + DEFAULT_RESOURCE_PATTERN;
        logger.debug("Type alias package *：" + typeAliasesPackage);
        try {
            Set<String> result = new HashSet<>(); // 别名包路径集合
            Resource[] resources = resolver.getResources(typeAliasesPackage); // 根据路径 读取所有的类资源
            if (resources != null && resources.length > 0) {
                MetadataReader metadataReader = null;
                for (Resource resource : resources) {
                    if (resource.isReadable()) {
                        metadataReader = metadataReaderFactory.getMetadataReader(resource); // 读取类的信息，每个 Resource 都是一个类资源
                        try {
                            result.add(Class.forName(metadataReader.getClassMetadata().getClassName()).getPackage().getName()); // 存储类对应的包路径
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (result.size() > 0) {
                logger.debug("Type alias package collection：" + StringUtils.join(result.toArray(), ","));
                super.setTypeAliasesPackage(StringUtils.join(result.toArray(), ","));
            } else {
                logger.warn("Parameter typeAliasesPackage:" + typeAliasesPackage + ", not found any packages.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
