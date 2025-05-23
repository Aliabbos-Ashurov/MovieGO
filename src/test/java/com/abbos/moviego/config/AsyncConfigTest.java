package com.abbos.moviego.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
@SpringBootTest(classes = AsyncConfig.class)
class AsyncConfigTest {

    @Autowired
    private Executor asyncExecutor;

    @Test
    void asyncExecutor_isConfiguredCorrectly() {
        assertNotNull(asyncExecutor);
        assertInstanceOf(ThreadPoolTaskExecutor.class, asyncExecutor);

        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) asyncExecutor;
        assertEquals("async-exec-", executor.getThreadNamePrefix());
        assertEquals(10, executor.getCorePoolSize());
        assertEquals(50, executor.getMaxPoolSize());
        assertEquals(500, executor.getQueueCapacity());
        assertEquals(60, executor.getKeepAliveSeconds());
    }
}
