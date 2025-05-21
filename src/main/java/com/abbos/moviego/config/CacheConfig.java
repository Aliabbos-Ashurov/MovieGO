package com.abbos.moviego.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-20
 */
@EnableCaching
@Configuration(proxyBeanMethods = false)
public class CacheConfig {
}
