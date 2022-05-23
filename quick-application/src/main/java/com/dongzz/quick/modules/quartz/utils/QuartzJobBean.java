package com.dongzz.quick.modules.quartz.utils;

import cn.hutool.core.bean.BeanUtil;
import com.dongzz.quick.common.utils.RedisUtil;
import com.dongzz.quick.common.utils.SpringContextHolder;
import com.dongzz.quick.common.utils.StringUtil;
import com.dongzz.quick.common.utils.ThrowableUtil;
import com.dongzz.quick.common.utils.thread.ThreadPoolExecutorUtil;
import com.dongzz.quick.modules.quartz.domain.SysQuartzJob;
import com.dongzz.quick.logging.domain.SysQuartzLog;
import com.dongzz.quick.modules.quartz.service.QuartzJobService;
import com.dongzz.quick.logging.service.QuartzLogService;
import com.dongzz.quick.tools.service.MailService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 定时任务 核心调度对象
 * 每次执行定时任务，都会调用 executeInternal() 并且将任务信息传递过来
 */
public class QuartzJobBean extends org.springframework.scheduling.quartz.QuartzJobBean {

    public static final ThreadPoolExecutor EXECUTORS = ThreadPoolExecutorUtil.getPoll();

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        // 获取当前任务信息
        Object job = context.getMergedJobDataMap().get(SysQuartzJob.JOB_KEY);
        // 此处手动创建任务，不能将获取的任务信息直接转型，会出错
        SysQuartzJob quartzJob = new SysQuartzJob();
        BeanUtil.copyProperties(job, quartzJob);

        // 获取 spring bean
        QuartzJobService quartzJobService = SpringContextHolder.getBean(QuartzJobService.class);
        QuartzLogService quartzLogService = SpringContextHolder.getBean(QuartzLogService.class);
        RedisUtil redisUtil = SpringContextHolder.getBean(RedisUtil.class);

        // 子任务标记
        String uuid = quartzJob.getUuid();

        // 记录日志
        SysQuartzLog log = new SysQuartzLog();
        log.setJobName(quartzJob.getJobName());
        log.setBeanName(quartzJob.getSpringBeanName());
        log.setMethodName(quartzJob.getMethodName());
        log.setParams(quartzJob.getParams());
        log.setCron(quartzJob.getCron());

        long startTime = System.currentTimeMillis(); // 起始时间
        try {

            // 执行任务
            System.out.println("-------------------------------------------");
            System.out.println("执行任务开始，任务名称：" + quartzJob.getJobName());
            QuartzCallable task = new QuartzCallable(quartzJob.getSpringBeanName(), quartzJob.getMethodName(), quartzJob.getParams());
            Future<Object> future = EXECUTORS.submit(task);
            future.get();
            long times = System.currentTimeMillis() - startTime; // 结束时间
            log.setTime(times);
            if (StringUtil.isNotBlank(uuid)) {
                redisUtil.set(uuid, true);
            }
            log.setIsSuccess(true);
            System.out.println("执行任务完毕，任务名称：" + quartzJob.getJobName() + "，执行耗时：" + times + "毫秒");
            System.out.println("--------------------------------------------");

            // 判断是否存在子任务
            if (quartzJob.getSubTask() != null) {
                String[] tasks = quartzJob.getSubTask().split("[,，]");
                quartzJobService.executeQuartzSubJob(tasks); // 执行子任务
            }
        } catch (Exception e) {
            if (StringUtil.isNotBlank(uuid)) {
                redisUtil.set(uuid, false);
            }
            System.out.println("执行任务失败，任务名称：" + quartzJob.getJobName());
            System.out.println("---------------------------------------------");
            long times = System.currentTimeMillis() - startTime;
            log.setTime(times);
            log.setIsSuccess(false);
            log.setException(ThrowableUtil.getStackTrace(e)); // 异常信息
            // 若任务失败，则暂停
            if (null != quartzJob.getPauseAfterFailure() && quartzJob.getPauseAfterFailure()) {
                quartzJob.setStatus(true);
                try {
                    quartzJobService.updateStatus(quartzJob);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            // 邮件报警
            if (quartzJob.getEmail() != null) {
                MailService mailService = SpringContextHolder.getBean(MailService.class);
                if (StringUtil.isNotBlank(quartzJob.getEmail())) {
                    // 报警邮件投递

                }
            }
        } finally {
            try {
                log.setCreateTime(new Date());
                quartzLogService.insertSelective(log); // 存储日志
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
