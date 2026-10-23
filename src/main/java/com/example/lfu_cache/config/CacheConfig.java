package com.example.lfu_cache.config;

import com.example.lfu_cache.cache.Cache;
import com.example.lfu_cache.cache.LfuCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Value("${cache.capacity:3}")
    private int capacity;

    @Bean
    public Cache<String, String> lfuCache() {
        return new LfuCache<>(capacity);
    }
}