package com.training.spring.lab.config.cache;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntityHandlerConfiguration {

    @Bean
    public CacheEvictHandler cacheEvictHandler(final CacheManager cacheManager) {
        return new CacheEvictHandler(cacheManager);
    }

}
