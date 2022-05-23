package com.dongzz.quick.logging.dao;

import com.dongzz.quick.common.base.BaseMybatisMapper;
import com.dongzz.quick.logging.domain.SysLogs;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 系统日志 数据访问接口
 */
public interface SysLogsMapper extends BaseMybatisMapper<SysLogs> {

    /**
     * 删除，逻辑
     *
     * @param logsId 日志ID
     * @throws Exception
     */
    void deleteLogs(Long logsId) throws Exception;

    /**
     * 条件查询 统计数量
     *
     * @param params 查询条件
     * @return
     * @throws Exception
     */
    Integer count(@Param("params") Map<String, Object> params) throws Exception;

    /**
     * 条件查询 分页
     *
     * @param params 查询条件 包含排序
     * @param offset 每页起始索引
     * @param limit  每页显示条数
     * @return
     * @throws Exception
     */
    List<SysLogs> selectAllLogs(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
                                @Param("limit") Integer limit) throws Exception;
}
