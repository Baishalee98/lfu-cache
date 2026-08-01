package com.example.lfu_cache.cache;

import java.util.HashMap;
import java.util.Map;

public class LfuCache<K, V> {

    private final int capacity;
    private final Map<K, CacheNode<K, V>> mainStore;
    private final CacheMetrics metrics;

    public LfuCache(int capacity) {
        this.capacity = capacity;
        this.mainStore = new HashMap<>();
        this.metrics = new CacheMetrics();
    }

    public synchronized V get(K key) {
        if (!mainStore.containsKey(key)) {
            metrics.recordMiss();
            return null;
        }

        CacheNode<K, V> node = mainStore.get(key);
        node.incrementFrequency();
        node.touch();
        metrics.recordHit();

        return node.getValue();
    }

    public synchronized void put(K key, V value) {
        if (capacity <= 0) return;

        if (mainStore.containsKey(key)) {
            CacheNode<K, V> node = mainStore.get(key);
            node.setValue(value);
            node.incrementFrequency();
            node.touch();
            return;
        }

        if (mainStore.size() >= capacity) {
            evict();
        }

        CacheNode<K, V> newNode = new CacheNode<>(key, value);
        mainStore.put(key, newNode);
    }

    private void evict() {
        K keyToEvict = null;
        int minFreq = Integer.MAX_VALUE;
        long oldestTime = Long.MAX_VALUE;

        // Linear loop over all elements in mainStore
        for (Map.Entry<K, CacheNode<K, V>> entry : mainStore.entrySet()) {
            // INSTRUMENTATION: Track element inspection
            ComplexityCounter.increment();

            CacheNode<K, V> node = entry.getValue();
            if (node.getFrequency() < minFreq) {
                minFreq = node.getFrequency();
                oldestTime = node.getLastAccessedTime();
                keyToEvict = entry.getKey();
            } else if (node.getFrequency() == minFreq) {
                if (node.getLastAccessedTime() < oldestTime) {
                    oldestTime = node.getLastAccessedTime();
                    keyToEvict = entry.getKey();
                }
            }
        }

        if (keyToEvict != null) {
            mainStore.remove(keyToEvict);
            metrics.recordEviction();
        }
    }

    public synchronized int size() {
        return mainStore.size();
    }

    public CacheMetrics getMetrics() {
        return metrics;
    }
}
