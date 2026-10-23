package com.example.lfu_cache.cache;

import java.util.concurrent.atomic.AtomicLong;

public class ComplexityCounter {
    private static final AtomicLong COUNTER = new AtomicLong(0);

    public static void reset() { COUNTER.set(0); }
    public static void increment() { COUNTER.incrementAndGet(); }
    public static long get() { return COUNTER.get(); }
}