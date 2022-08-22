package com.seeds.admin.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * 线程池配置
 *
 * @author hang.yu
 * @date 2022/8/18
 */

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ExecutorService executorService() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("thread-call-runner-%d").build();
        return new ThreadPoolExecutor(3, 5, 200L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(1), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

}
