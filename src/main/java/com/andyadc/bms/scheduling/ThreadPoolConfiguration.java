package com.andyadc.bms.scheduling;

import com.andyadc.bms.common.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfiguration {

    @Primary
    @Bean
    public ThreadPoolTaskExecutor defaultTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("DefaultTask-");
        executor.setCorePoolSize(Constants.PROCESSOR_NUM);
        executor.setMaxPoolSize(Constants.PROCESSOR_NUM * 2);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setRejectedExecutionHandler(rejectedExecutionHandler());

        return executor;
    }

    private RejectedExecutionHandler rejectedExecutionHandler() {
        return new ThreadPoolExecutor.CallerRunsPolicy();
    }

}
