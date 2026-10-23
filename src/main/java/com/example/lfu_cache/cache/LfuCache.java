package com.example.lfu_cache.cache;

import java.util.HashMap;
import java.util.Map;

public class LfuCache<K, V> implements Cache<K, V> {

    private final int capacity;
    private final Map<K, CacheNode<K, V>> mainStore;
    private final Map<Integer, FrequencyBucket<K, V>> frequencyBuckets;
    private final CacheMetrics metrics;
    private int minFrequency;

    public LfuCache(int capacity) {
        this.capacity = capacity;
        this.mainStore = new HashMap<>();
        this.frequencyBuckets = new HashMap<>();
        this.metrics = new CacheMetrics();
        this.minFrequency = 0;
    }

    @Override
    public synchronized V get(K key) {
        if (!mainStore.containsKey(key)) {
            metrics.recordMiss();
            return null;
        }

        CacheNode<K, V> node = mainStore.get(key);
        updateFrequency(node);
        metrics.recordHit();

        return node.getValue();
    }

    @Override
    public synchronized void put(K key, V value) {
        if (capacity <= 0) return;

        if (mainStore.containsKey(key)) {
            CacheNode<K, V> node = mainStore.get(key);
            node.setValue(value);
            updateFrequency(node);
            return;
        }

        if (mainStore.size() >= capacity) {
            evict();
        }

        CacheNode<K, V> newNode = new CacheNode<>(key, value);
        mainStore.put(key, newNode);
        addToFrequencyBucket(newNode, 1);
        minFrequency = 1;
    }

    private void updateFrequency(CacheNode<K, V> node) {
        int oldFreq = node.getFrequency();
        int newFreq = oldFreq + 1;

        removeFromFrequencyBucket(node, oldFreq);
        node.incrementFrequency();
        node.touch();
        addToFrequencyBucket(node, newFreq);

        if (oldFreq == minFrequency && frequencyBuckets.get(oldFreq).isEmpty()) {
            minFrequency = newFreq;
        }
    }

    private void addToFrequencyBucket(CacheNode<K, V> node, int frequency) {
        FrequencyBucket<K, V> bucket = frequencyBuckets.computeIfAbsent(frequency, k -> new FrequencyBucket<>());
        bucket.addToTail(node);
    }

    private void removeFromFrequencyBucket(CacheNode<K, V> node, int frequency) {
        FrequencyBucket<K, V> bucket = frequencyBuckets.get(frequency);
        if (bucket != null) {
            bucket.remove(node);
        }
    }

    private void evict() {
        ComplexityCounter.increment();

        FrequencyBucket<K, V> bucket = frequencyBuckets.get(minFrequency);
        if (bucket == null || bucket.isEmpty()) {
            return;
        }

        CacheNode<K, V> nodeToEvict = bucket.getFirst();
        bucket.remove(nodeToEvict);
        mainStore.remove(nodeToEvict.getKey());
        metrics.recordEviction();
    }

    @Override
    public synchronized int size() {
        return mainStore.size();
    }

    @Override
    public CacheMetrics getMetrics() {
        return metrics;
    }
}