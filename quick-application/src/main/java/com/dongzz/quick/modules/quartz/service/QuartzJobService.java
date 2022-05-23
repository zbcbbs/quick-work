package com.dongzz.quick.modules.quartz.service;

import com.dongzz.quick.common.base.BaseMybatisService;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.modules.quartz.domain.SysQuartzJob;

import java.util.List;
import java.util.Set;

/**
 * 定时任务 相关服务接口
 */
public interface QuartzJobService extends BaseMybatisService<SysQuartzJob> {

    /**
     * 新增
     *
     * @param quartzJob
     * @throws Exception
     */
    void addQuartzJob(SysQuartzJob quartzJob) throws Exception;

    /**
     * 删除，支持批量
     *
     * @param id
     * @throws Exception
     */
    void deleteQuartzJob(String id) throws Exception;

    /**
     * 修改
     *
     * @param quartzJob
     * @throws Exception
     */
    void updateQuartzJob(SysQuartzJob quartzJob) throws Exception;

    /**
     * 修改任务状态
     *
     * @param quartzJob
     * @throws Exception
     */
    void updateStatus(SysQuartzJob quartzJob) throws Exception;

    /**
     * 执行
     *
     * @param quartzJob
     * @throws Exception
     */
    void executeQuartzJob(SysQuartzJob quartzJob) throws Exception;

    /**
     * 执行子任务
     *
     * @param tasks 子任务数组
     * @throws Exception
     */
    void executeQuartzSubJob(String[] tasks) throws Exception;


    /**
     * 获取 spring ioc 中的bean名称列表
     *
     * @return
     * @throws Exception
     */
    List<String> getSpringBeanNames() throws Exception;

    /**
     * 获取bean中无参方法列表
     *
     * @param name bean名称
     * @return
     * @throws Exception
     */
    Set<String> getBeanMethodNames(String name) throws Exception;

    /**
     * 条件和分页查询
     *
     * @param request
     * @return
     * @throws Exception
     */
    VueTableResponse findAll(VueTableRequest request) throws Exception;

}
