package com.example.lfu_cache.cache;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Tracks element inspections across cache operations to test O(1) time complexity.
 */
public class ComplexityCounter {
    private static final AtomicLong COUNTER = new AtomicLong(0);

    public static void reset() {
        COUNTER.set(0);
    }

    public static void increment() {
        COUNTER.incrementAndGet();
    }

    public static long get() {
        return COUNTER.get();
    }
}