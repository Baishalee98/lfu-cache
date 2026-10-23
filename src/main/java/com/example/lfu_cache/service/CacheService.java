package com.example.lfu_cache.service;

import com.example.lfu_cache.cache.Cache;
import com.example.lfu_cache.cache.CacheMetrics;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    private final Cache<String, String> cache;

    public CacheService(Cache<String, String> cache) {
        this.cache = cache;
    }

    public String get(String key) {
        return cache.get(key);
    }

    public void put(String key, String value) {
        cache.put(key, value);
    }

    public CacheMetrics getMetrics() {
        return cache.getMetrics();
    }
}