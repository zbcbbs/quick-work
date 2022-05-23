package com.dongzz.quick.generator.service;

import com.dongzz.quick.common.base.BaseMybatisService;
import com.dongzz.quick.generator.domain.CodeGenConfig;

/**
 * 代码生成配置 相关服务接口
 */
public interface GenConfigService extends BaseMybatisService<CodeGenConfig> {

    /**
     * 修改表配置
     *
     * @param config 配置
     * @return
     * @throws Exception
     */
    void updateConfig(CodeGenConfig config) throws Exception;

    /**
     * 查询表配置
     *
     * @param tableName 表名
     * @return
     * @throws Exception
     */
    CodeGenConfig findByTableName(String tableName) throws Exception;


}
