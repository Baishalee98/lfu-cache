package com.example.lfu_cache.cache;

public interface Cache<K, V> {
    V get(K key);
    void put(K key, V value);
    int size();
    CacheMetrics getMetrics();
}