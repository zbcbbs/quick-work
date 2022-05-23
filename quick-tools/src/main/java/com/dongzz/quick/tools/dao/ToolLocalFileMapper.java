package com.dongzz.quick.tools.dao;

import com.dongzz.quick.common.base.BaseMybatisMapper;
import com.dongzz.quick.tools.domain.ToolLocalFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 本地存储 数据访问接口
 */
public interface ToolLocalFileMapper extends BaseMybatisMapper<ToolLocalFile> {

    /**
     * 条件查询 统计数量
     *
     * @param params
     * @return
     * @throws Exception
     */
    int count(@Param("params") Map<String, Object> params) throws Exception;

    /**
     * 条件和分页查询
     *
     * @param params 查询条件
     * @param offset 起始索引
     * @param limit  分页单位
     * @return
     * @throws Exception
     */
    List<ToolLocalFile> selectAllFiles(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit) throws Exception;

}