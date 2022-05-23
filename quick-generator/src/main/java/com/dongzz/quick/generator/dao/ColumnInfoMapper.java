package com.dongzz.quick.generator.dao;

import com.dongzz.quick.common.base.BaseMybatisMapper;
import com.dongzz.quick.generator.domain.CodeColumnInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 字段信息 数据访问接口
 */
public interface ColumnInfoMapper extends BaseMybatisMapper<CodeColumnInfo> {

    /**
     * 查询表字段信息
     *
     * @param tableName 表名
     * @return
     */
    List<CodeColumnInfo> selectByTableName(String tableName) throws Exception;

    /**
     * 统计数量
     *
     * @param params 条件
     * @return
     * @throws Exception
     */
    Integer count(@Param("params") Map<String, Object> params) throws Exception;

    /**
     * 条件分页查询
     *
     * @param params 条件
     * @param offset 起始索引
     * @param limit  分页单位
     * @return
     * @throws Exception
     */
    List<CodeColumnInfo> selectAllColumns(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit) throws Exception;
}
