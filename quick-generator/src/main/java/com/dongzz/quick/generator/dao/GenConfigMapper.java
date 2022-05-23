package com.dongzz.quick.generator.dao;

import com.dongzz.quick.common.base.BaseMybatisMapper;
import com.dongzz.quick.generator.domain.CodeGenConfig;

/**
 * 代码生成配置 数据访问接口
 */
public interface GenConfigMapper extends BaseMybatisMapper<CodeGenConfig> {

    /**
     * 查询表配置
     *
     * @param tableName 表名
     * @return
     */
    CodeGenConfig selectByTableName(String tableName) throws Exception;


}
