package com.example.lfu_cache.integration;

import com.example.lfu_cache.cache.LfuCache;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CacheConcurrencyTest {

    @Test
    @DisplayName("High concurrent reads and writes should remain non-corrupted and within capacity")
    void testConcurrentPutAndGet() throws InterruptedException {
        int capacity = 50;
        LfuCache<String, String> cache = new LfuCache<>(capacity);

        int threadCount = 20;
        int keysPerThread = 500;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger nullValueErrors = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < keysPerThread; j++) {
                        String key = "Key-" + (j % 100);
                        String val = "Val-" + threadId + "-" + j;

                        cache.put(key, val);

                        String retrieved = cache.get(key);
                        // If retrieved is not null, it should be a valid string prefix
                        if (retrieved != null && !retrieved.startsWith("Val-")) {
                            nullValueErrors.incrementAndGet();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        assertTrue(cache.size() <= capacity, "Cache size exceeded specified capacity under concurrent load");
        assertTrue(nullValueErrors.get() == 0, "Corrupted value states detected during concurrent operations");
    }
}