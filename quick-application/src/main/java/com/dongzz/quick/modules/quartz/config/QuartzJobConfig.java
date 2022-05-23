package com.dongzz.quick.modules.quartz.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * spring quartz 定时任务配置
 */
@Configuration
@EnableScheduling
public class QuartzJobConfig {

    /**
     * Job Bean 默认由 Quartz 管理
     * 若需要让spring来管理，也就是支持依赖注入，那么需要指定 JobFactory
     */
    @Component("quartzJobFactory")
    public static class QuartzJobFactory extends AdaptableJobFactory {

        private final AutowireCapableBeanFactory capableBeanFactory;

        public QuartzJobFactory(AutowireCapableBeanFactory capableBeanFactory) {
            this.capableBeanFactory = capableBeanFactory;
        }

        /**
         * 创建 Job 实例
         */
        @Override
        protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {

            // 实例化对象
            Object jobInstance = super.createJobInstance(bundle);
            // 自动注入，将该实例交由spring管理，让其支持依赖注入
            capableBeanFactory.autowireBean(jobInstance);
            return jobInstance;
        }
    }

    /**
     * 调度工厂
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, QuartzJobFactory quartzJobFactory) throws Exception {
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();

        try {
            // quartz 核心配置
            factoryBean.setQuartzProperties(PropertiesLoaderUtils.loadProperties(new ClassPathResource(
                    "quartz.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        factoryBean.setDataSource(dataSource); // 数据源
        factoryBean.setJobFactory(quartzJobFactory); // 任务工厂
        factoryBean.setOverwriteExistingJobs(true); // 任务已存在 覆盖
        factoryBean.setStartupDelay(10); // 任务启动延迟10秒
        return factoryBean;
    }


}
