package com.dongzz.quick.generator.service.impl;

import com.dongzz.quick.common.base.BaseMybatisServiceImpl;
import com.dongzz.quick.generator.dao.GenConfigMapper;
import com.dongzz.quick.generator.domain.CodeGenConfig;
import com.dongzz.quick.generator.service.GenConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GenConfigServiceImpl extends BaseMybatisServiceImpl<CodeGenConfig> implements GenConfigService {

    @Autowired
    private GenConfigMapper configMapper;

    @Override
    @Transactional
    public void updateConfig(CodeGenConfig config) throws Exception {
        if (config.getId() != null) {
            configMapper.updateByPrimaryKeySelective(config);
        } else {
            configMapper.insertSelective(config);
        }
    }

    @Override
    public CodeGenConfig findByTableName(String tableName) throws Exception {
        CodeGenConfig config = configMapper.selectByTableName(tableName);
        if (config == null) {
            config = new CodeGenConfig(tableName);
            config.setCover(false);
        }
        return config;
    }

}
