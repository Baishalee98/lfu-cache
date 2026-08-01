package com.example.lfu_cache.cache;

public class CacheMetrics {
    private long hits = 0;
    private long misses = 0;
    private long evictions = 0;

    // TODO: Evaluate if standard primitive long counters are adequate
    // under high concurrent request loads as specified in README requirements.
    public void recordHit() { hits++; }
    public void recordMiss() { misses++; }
    public void recordEviction() { evictions++; }

    public long getHits() { return hits; }
    public long getMisses() { return misses; }
    public long getEvictions() { return evictions; }

    public double getHitRate() {
        long total = hits + misses;
        return total == 0 ? 0.0 : (double) hits / total;
    }
}