package com.example.lfu_cache.cache;

import java.util.concurrent.atomic.LongAdder;

public class CacheMetrics {
    private final LongAdder hits = new LongAdder();
    private final LongAdder misses = new LongAdder();
    private final LongAdder evictions = new LongAdder();

    public void recordHit() { hits.increment(); }
    public void recordMiss() { misses.increment(); }
    public void recordEviction() { evictions.increment(); }

    public long getHits() { return hits.sum(); }
    public long getMisses() { return misses.sum(); }
    public long getEvictions() { return evictions.sum(); }

    public double getHitRate() {
        long total = hits.sum() + misses.sum();
        return total == 0 ? 0.0 : (double) hits.sum() / total;
    }
}