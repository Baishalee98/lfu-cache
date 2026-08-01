package com.example.lfucache.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Primary LFU Cache Implementation.
 */
public class LfuCache<K, V> {

    private final int capacity;
    private final Map<K, CacheNode<K, V>> mainStore;
    private final CacheMetrics metrics;

    public LfuCache(int capacity) {
        this.capacity = capacity;
        this.mainStore = new HashMap<>();
        this.metrics = new CacheMetrics();
    }

    /**
     * Retrieves an entry from the cache.
     * TODO: Review complexity and concurrency characteristics.
     */
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

    /**
     * Inserts or updates an entry in the cache.
     *
     * TODO: Instructions for Developer:
     * 1. Fix algorithmic complexity: Current eviction strategy scans the entire map,
     *    resulting in O(N) complexity instead of the required O(1) in README.
     * 2. Ensure tie-breaking strictly follows LRU order for items with identical minimum frequency.
     * 3. Address lock contention: Using 'synchronized' on every method creates a global bottleneck.
     * 4. Fix potential bug: Does updating an existing key handle frequency increments correctly
     *    without replacing the frequency state or triggering unintended evictions?
     */
    public synchronized void put(K key, V value) {
        if (capacity <= 0) return;

        if (mainStore.containsKey(key)) {
            CacheNode<K, V> node = mainStore.get(key);
            node.setValue(value);
            // Developer Note: Should updating an existing key increment frequency?
            // Refer to README requirement 5.
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

    /**
     * Linear scan eviction - FLUTTERING O(N) IMPLEMENTATION
     */
    private void evict() {
        K keyToEvict = null;
        int minFreq = Integer.MAX_VALUE;
        long oldestTime = Long.MAX_VALUE;

        for (Map.Entry<K, CacheNode<K, V>> entry : mainStore.entrySet()) {
            CacheNode<K, V> node = entry.getValue();
            if (node.getFrequency() < minFreq) {
                minFreq = node.getFrequency();
                oldestTime = node.getLastAccessedTime();
                keyToEvict = entry.getKey();
            } else if (node.getFrequency() == minFreq) {
                // Check LRU timestamp
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