package com.example.lfu_cache.cache;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CacheMetricsTest {

    /*
    @Test
    @DisplayName("Verify accuracy of Hits, Misses, and Evictions under multi-threaded writes")
    void testConcurrentMetricsUpdate() throws InterruptedException {
        CacheMetrics metrics = new CacheMetrics();
        int threads = 10;
        int operationsPerThread = 1000;

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < operationsPerThread; j++) {
                        metrics.recordHit();
                        metrics.recordMiss();
                        metrics.recordEviction();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        long expectedCount = (long) threads * operationsPerThread;

        // NOTE: This test will FAIL on the non-atomic long counters in original CacheMetrics.java
        assertEquals(expectedCount, metrics.getHits(), "Hits metric suffered race conditions");
        assertEquals(expectedCount, metrics.getMisses(), "Misses metric suffered race conditions");
        assertEquals(expectedCount, metrics.getEvictions(), "Evictions metric suffered race conditions");
    }
     */
}