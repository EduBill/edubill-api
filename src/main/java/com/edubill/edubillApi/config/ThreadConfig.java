package com.edubill.edubillApi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class ThreadConfig {

    // 빈으로 등록된 TaskExecutor이 하나인 경우 그를 사용하고,
    // 2개 이상인 경우 이름이 “taskExecutor”인 빈을 사용한다.
    // 만약 이름이 “taskExecutor”인 빈이 없을 경우, SimpleAsyncTaskExecutor을 대신 사용한다.
    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor executor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setQueueCapacity(5);
        executor.setMaxPoolSize(10);

        //새로운 작업이 스레드 풀에 추가되지 못하더라도, 작업을 호출한 스레드에서 작업을 처리하므로 스레드 풀의 크기를 초과하는 부하가 발생하지 않음.
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}
