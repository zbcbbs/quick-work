package com.dongzz.quick.modules.quartz.utils;

import com.dongzz.quick.common.exception.ApplicationException;
import com.dongzz.quick.modules.quartz.domain.SysQuartzJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时任务 工具
 */
@Component
public class QuartzJobUtil {

    public static final String JOB_NAME = "TASK_";

    @Autowired
    private Scheduler scheduler;


    /**
     * 添加
     *
     * @param quartzJob
     */
    public void addJob(SysQuartzJob quartzJob) {
        try {
            // 任务标记
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());

            // 创建定时任务
            JobDetail jobDetail = JobBuilder
                    .newJob(QuartzJobBean.class) // 核心调度器
                    .storeDurably() // 持久化 触发器删除时不删除关联任务
                    .withIdentity(jobKey) // 任务标记
                    .withDescription(quartzJob.getDescription()) // 描述
                    .build();
            jobDetail.getJobDataMap().put(SysQuartzJob.JOB_KEY, quartzJob); // 存储任务信息

            // 创建触发器
            CronTrigger cronTrigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(JOB_NAME + quartzJob.getId()) // 触发器标记
                    .withSchedule(CronScheduleBuilder.cronSchedule(quartzJob.getCron())) // cron 表达式
                    .forJob(jobKey) // 关联定时任务
                    .build();

            // 添加并启动
            scheduler.scheduleJob(jobDetail, cronTrigger);

            // 判断状态
            if (!quartzJob.getStatus()) {
                pauseJob(quartzJob);
            }
        } catch (Exception e) {
            throw new ApplicationException("创建定时任务失败", e);
        }

    }

    /**
     * 修改
     *
     * @param quartzJob
     */
    public void updateJob(SysQuartzJob quartzJob) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getId()); // 触发器标记
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey); // 获取任务对应的触发器
            // 判断任务是否存在 不存在，则创建
            if (trigger == null) {
                addJob(quartzJob);
                trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            }

            trigger = trigger.getTriggerBuilder()
                    .withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(quartzJob.getCron())) // 修改 cron 表达式
                    .build();

            // 修改
            scheduler.rescheduleJob(triggerKey, trigger);

            // 判断状态
            if (!quartzJob.getStatus()) {
                pauseJob(quartzJob);
            }
        } catch (Exception e) {
            throw new ApplicationException("刷新定时任务失败", e);
        }
    }

    /**
     * 删除
     *
     * @param quartzJob
     */
    public void deleteJob(SysQuartzJob quartzJob) {
        try {
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId()); // 任务标记
            scheduler.pauseJob(jobKey); // 暂停
            scheduler.deleteJob(jobKey); // 删除
        } catch (Exception e) {
            throw new ApplicationException("删除定时任务失败");
        }

    }

    /**
     * 暂停
     *
     * @param quartzJob
     */
    public void pauseJob(SysQuartzJob quartzJob) {
        try {
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
            scheduler.pauseJob(jobKey);
        } catch (Exception e) {
            throw new ApplicationException("暂停定时任务失败", e);
        }
    }

    /**
     * 恢复
     *
     * @param quartzJob
     */
    public void resumeJob(SysQuartzJob quartzJob) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getId());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                addJob(quartzJob);
            }
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
            scheduler.resumeJob(jobKey); // 恢复
        } catch (Exception e) {
            throw new ApplicationException("恢复定时任务失败", e);
        }
    }

    /**
     * 立即执行
     *
     * @param quartzJob
     */
    public void executeJob(SysQuartzJob quartzJob) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(JOB_NAME + quartzJob.getId());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                addJob(quartzJob);
            }
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(SysQuartzJob.JOB_KEY, quartzJob);
            JobKey jobKey = JobKey.jobKey(JOB_NAME + quartzJob.getId());
            scheduler.triggerJob(jobKey, dataMap);
        } catch (Exception e) {
            throw new ApplicationException("执行定时任务失败", e);
        }
    }

}
