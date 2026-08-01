package com.example.lfu_cache.cache;

public class CacheNode<K, V> {
    private final K key;
    private V value;
    private int frequency;
    private long lastAccessedTime;
    CacheNode<K, V> prev;
    CacheNode<K, V> next;

    public CacheNode(K key, V value) {
        this.key = key;
        this.value = value;
        this.frequency = 1;
        this.lastAccessedTime = System.nanoTime();
    }

    public K getKey() { return key; }
    public V getValue() { return value; }
    public void setValue(V value) { this.value = value; }
    public int getFrequency() { return frequency; }
    public void setFrequency(int frequency) { this.frequency = frequency; }
    public void incrementFrequency() { this.frequency++; }
    public long getLastAccessedTime() { return lastAccessedTime; }
    public void touch() { this.lastAccessedTime = System.nanoTime(); }
}