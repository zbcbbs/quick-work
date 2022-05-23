package com.dongzz.quick.modules.quartz.service.impl;

import cn.hutool.core.util.IdUtil;
import com.dongzz.quick.common.annotation.schedule.Scheduled;
import com.dongzz.quick.common.base.BaseMybatisServiceImpl;
import com.dongzz.quick.common.exception.ServiceException;
import com.dongzz.quick.common.plugin.vuetables.VueTableHandler;
import com.dongzz.quick.common.plugin.vuetables.VueTableRequest;
import com.dongzz.quick.common.plugin.vuetables.VueTableResponse;
import com.dongzz.quick.common.utils.RedisUtil;
import com.dongzz.quick.common.utils.SpringContextHolder;
import com.dongzz.quick.common.utils.StringUtil;
import com.dongzz.quick.common.utils.Util;
import com.dongzz.quick.modules.quartz.domain.SysQuartzJob;
import com.dongzz.quick.modules.quartz.dao.SysQuartzJobMapper;
import com.dongzz.quick.modules.quartz.service.QuartzJobService;
import com.dongzz.quick.modules.quartz.utils.QuartzJobUtil;
import org.quartz.*;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

@Service
@Transactional
public class QuartzJobServiceImpl extends BaseMybatisServiceImpl<SysQuartzJob> implements QuartzJobService {

    @Autowired
    private SysQuartzJobMapper quartzJobMapper;
    @Autowired
    private QuartzJobUtil quartzJobUtil;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void addQuartzJob(SysQuartzJob quartzJob) throws Exception {
        // 检验 cron 表达式
        if (!CronExpression.isValidExpression(quartzJob.getCron())) {
            throw new ServiceException("Cron表达式格式不正确");
        }
        // 新增 获取主键
        quartzJob.setCreateTime(new Date());
        quartzJob.setUpdateTime(new Date());
        quartzJobMapper.insertQuartzJob(quartzJob);
        quartzJobUtil.addJob(quartzJob);
    }

    @Override
    public void deleteQuartzJob(String id) throws Exception {
        if (StringUtil.isNotBlank(id)) {
            if (id.contains(",")) {
                List<String> ids = Util.strSplitToList(id, ",");
                for (String jid : ids) {
                    SysQuartzJob quartzJob = quartzJobMapper.selectByPrimaryKey(Integer.valueOf(jid));
                    quartzJobUtil.deleteJob(quartzJob);
                    quartzJobMapper.delete(quartzJob);
                }
            } else {
                SysQuartzJob quartzJob = quartzJobMapper.selectByPrimaryKey(Integer.valueOf(id));
                quartzJobUtil.deleteJob(quartzJob);
                quartzJobMapper.delete(quartzJob);
            }
        }
    }

    @Override
    public void updateQuartzJob(SysQuartzJob quartzJob) throws Exception {
        if (!CronExpression.isValidExpression(quartzJob.getCron())) {
            throw new ServiceException("Cron表达式格式不正确");
        }

        // 子任务检查
        if (StringUtil.isNotBlank(quartzJob.getSubTask())) {
            List<String> tasks = Arrays.asList(quartzJob.getSubTask().split("[,，]"));
            if (tasks.contains(quartzJob.getId().toString())) {
                throw new ServiceException("子任务中不能添加当前任务ID");
            }
        }

        quartzJob.setUpdateTime(new Date()); // 修改时间
        int i = quartzJobMapper.updateByPrimaryKeySelective(quartzJob);
        if (i > 0) {
            // 查询当前修改完成的
            SysQuartzJob job = quartzJobMapper.selectByPrimaryKey(quartzJob.getId());
            quartzJobUtil.updateJob(job);
        }
    }

    @Override
    public void updateStatus(SysQuartzJob quartzJob) throws Exception {
        if (quartzJob.getStatus()) {
            quartzJobUtil.pauseJob(quartzJob);
            quartzJob.setStatus(false);
        } else {
            quartzJob.setStatus(true);
            quartzJobUtil.resumeJob(quartzJob);
        }
        quartzJobMapper.updateByPrimaryKeySelective(quartzJob);
    }

    @Override
    public void executeQuartzJob(SysQuartzJob quartzJob) throws Exception {
        quartzJobUtil.executeJob(quartzJob);
    }

    @Async
    @Override
    public void executeQuartzSubJob(String[] tasks) throws Exception {
        for (String id : tasks) {
            SysQuartzJob quartzJob = selectByPk(Integer.parseInt(id));
            String uuid = IdUtil.simpleUUID(); // 子任务标记
            quartzJob.setUuid(uuid);
            executeQuartzJob(quartzJob); // 执行任务
            // 获取执行状态，若执行失败，则停止后面的子任务执行
            Boolean result = (Boolean) redisUtil.get(uuid);
            while (result == null) {
                // 休眠5秒，再次获取子任务执行情况
                Thread.sleep(5000);
                result = (Boolean) redisUtil.get(uuid);
            }
            if (!result) {
                redisUtil.del(uuid);
                break;
            }
        }
    }

    @Override
    public List<String> getSpringBeanNames() throws Exception {
        List<String> list = new ArrayList<>();
        String[] beanNames = SpringContextHolder.getApplicationContext().getBeanDefinitionNames();
        for (String beanName : beanNames) {
            if (beanName.contains(".")) {
                continue;
            }
            // 获取 bean Class
            Class<?> clazz = getClass(beanName);
            // 仅保留含有 @Scheduled 注解的 bean
            if (clazz.isAnnotationPresent(Scheduled.class)) {
                list.add(beanName);
            }
        }
        Collections.sort(list);
        return list;
    }

    public Set<String> getBeanMethodNames(String name) throws Exception {
        Class<?> clazz = getClass(name);
        Method[] methods = clazz.getDeclaredMethods();
        Set<String> methodNames = new HashSet<>();
        for (Method method : methods) {
            // 访问修饰符
            int modifier = method.getModifiers();
            if (Modifier.isPublic(modifier)) {
                boolean scheduled = method.isAnnotationPresent(Scheduled.class); // 含有 @Scheduled 标记
                Class<?>[] parameterTypes = method.getParameterTypes(); // 参数
                if (scheduled && parameterTypes.length == 0) {
                    methodNames.add(method.getName()); // 仅保留无参方法
                }
            }
        }

        return methodNames;
    }

    /**
     * 根据bean名称，获取对应的Class
     *
     * @param name bean 名称
     * @return
     */
    private Class<?> getClass(String name) {
        // 根据名称 获取bean
        Object object = SpringContextHolder.getBean(name);
        Class<?> clazz = object.getClass();
        // 代理类 无法校验注解 因此获取父类类型
        if (AopUtils.isAopProxy(object)) {
            clazz = clazz.getSuperclass();
        }
        return clazz;
    }

    @Override
    public VueTableResponse findAll(VueTableRequest request) throws Exception {
        VueTableHandler handler = new VueTableHandler(new VueTableHandler.CountHandler() {

            @Override
            public int count(VueTableRequest request) {
                try {
                    return quartzJobMapper.count(request.getParams());
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        }, new VueTableHandler.ListHandler() {

            @Override
            public List<?> list(VueTableRequest request) {
                try {
                    return quartzJobMapper.selectAllQuartzJobs(request.getParams(), request.getOffset(),
                            request.getLimit());
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ArrayList<>();
                }
            }
        }, new VueTableHandler.OrderHandler() {

            @Override
            public VueTableRequest order(VueTableRequest request) {
                Map<String, Object> params = request.getParams();
                if (null != params.get("orderBy")) {
                    String orderBy = params.get("orderBy").toString();
                    if (orderBy.contains("updateTime")) {
                        params.put("orderBy", orderBy.replace("updateTime", "update_time"));
                    }
                }
                return request;
            }
        });
        VueTableResponse response = handler.handle(request);
        return response;
    }

}
