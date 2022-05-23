package com.dongzz.quick.common.base;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Mybatis 通用Mapper接口，继承通用Mapper插件中的接口
 */
public interface BaseMybatisMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
