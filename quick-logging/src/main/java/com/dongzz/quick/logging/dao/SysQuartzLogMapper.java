package com.dongzz.quick.logging.dao;

import com.dongzz.quick.common.base.BaseMybatisMapper;
import com.dongzz.quick.logging.domain.SysQuartzLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 定时任务日志 访问接口
 */
public interface SysQuartzLogMapper extends BaseMybatisMapper<SysQuartzLog> {

    /**
     * 条件查询 统计数量
     *
     * @param params 条件
     * @return
     * @throws Exception
     */
    int count(@Param("params") Map<String, Object> params) throws Exception;

    /**
     * 分页和条件查询
     *
     * @param params 条件
     * @param offset 起始索引
     * @param limit  分页单位
     * @return
     * @throws Exception
     */
    List<SysQuartzLog> selectAllQuartzLogs(@Param("params") Map<String, Object> params,
                                           @Param("offset") Integer offset, @Param("limit") Integer limit) throws Exception;
}