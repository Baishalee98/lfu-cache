package com.example.lfu_cache.cache;

public interface EvictionPolicy<K, V> {
    void onAccess(CacheNode<K, V> node);
    void onInsert(CacheNode<K, V> node);
    CacheNode<K, V> evict();
    void remove(CacheNode<K, V> node);
}