package com.abbos.moviego.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-13
 */
@Configuration
public class AsyncConfig {

    @Bean
    public ExecutorService executorService() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
