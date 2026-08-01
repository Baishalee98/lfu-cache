package com.example.lfucache.service;

import com.example.lfucache.cache.LfuCache;
import com.example.lfucache.cache.CacheMetrics;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    private final LfuCache<String, String> cache;

    public CacheService(LfuCache<String, String> cache) {
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