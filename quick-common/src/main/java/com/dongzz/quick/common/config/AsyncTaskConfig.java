package com.dongzz.quick.common.config;

import com.dongzz.quick.common.config.bean.AsyncTaskProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步线程 相关配置
 */
@Slf4j
@EnableAsync
@Configuration
public class AsyncTaskConfig implements AsyncConfigurer {

    private final AsyncTaskProperties asyncProperties;

    public AsyncTaskConfig(AsyncTaskProperties asyncProperties) {
        this.asyncProperties = asyncProperties;
    }

    /**
     * 线程池
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(asyncProperties.getCorePoolSize()); // 核心线程池大小
        executor.setMaxPoolSize(asyncProperties.getMaxPoolSize()); // 最大线程数
        executor.setQueueCapacity(asyncProperties.getQueueCapacity()); // 队列容量
        executor.setKeepAliveSeconds(asyncProperties.getKeepAliveSeconds()); // 活跃时间
        executor.setThreadNamePrefix(asyncProperties.getThreadNamePrefix()); // 线程名字前缀
        // setRejectedExecutionHandler：当线程池已经达到max size的时候，如何处理新任务
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    /**
     * 异常捕获
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            log.error("====" + throwable.getMessage() + "====", throwable);
            log.error("exception method:" + method.getName()); // 出现异常的方法
        };
    }
}
