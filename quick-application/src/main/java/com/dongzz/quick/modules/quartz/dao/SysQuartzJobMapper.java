package com.dongzz.quick.modules.quartz.dao;

import com.dongzz.quick.common.base.BaseMybatisMapper;
import com.dongzz.quick.modules.quartz.domain.SysQuartzJob;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 定时任务 访问接口
 */
public interface SysQuartzJobMapper extends BaseMybatisMapper<SysQuartzJob> {

    /**
     * 新增定时任务，获取主键
     *
     * @param quartzJob
     * @return
     * @throws Exception
     */
    int insertQuartzJob(SysQuartzJob quartzJob) throws Exception;

    /**
     * 条件查询 统计数量
     *
     * @param params 查询条件
     * @return
     * @throws Exception
     */
    int count(@Param("params") Map<String, Object> params) throws Exception;

    /**
     * 分页和条件查询
     *
     * @param params 查询条件
     * @param offset 起始索引
     * @param limit  分页单位
     * @return
     * @throws Exception
     */
    List<SysQuartzJob> selectAllQuartzJobs(@Param("params") Map<String, Object> params,
                                           @Param("offset") Integer offset, @Param("limit") Integer limit) throws Exception;
}