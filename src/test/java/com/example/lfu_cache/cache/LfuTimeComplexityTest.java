package com.example.lfu_cache.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class LfuTimeComplexityTest {

    @BeforeEach
    void resetCounter() {
        ComplexityCounter.reset();
    }

    @Test
    @DisplayName("Assert O(1) Complexity Deterministically")
    void testO1TimeComplexityDeterministically() {
        int smallCapacity = 10;
        int largeCapacity = 1000;
        int evictionsToPerform = 50;

        ComplexityCounter.reset();
        Cache<String, String> smallCache = new LfuCache<>(smallCapacity);
        runEvictionWorkload(smallCache, smallCapacity, evictionsToPerform);
        long opsSmallN = ComplexityCounter.get();

        ComplexityCounter.reset();
        Cache<String, String> largeCache = new LfuCache<>(largeCapacity);
        runEvictionWorkload(largeCache, largeCapacity, evictionsToPerform);
        long opsLargeN = ComplexityCounter.get();

        if (opsSmallN == 0 || opsLargeN == 0) {
            fail("Counter recorded 0 operations!");
        }

        assertEquals(opsSmallN, opsLargeN, "Operation step count must be equal across capacities.");
    }

    private void runEvictionWorkload(Cache<String, String> cache, int capacity, int evictionsToTrigger) {
        for (int i = 0; i < capacity; i++) {
            cache.put("warmup_key_" + i, "val_" + i);
        }
        ComplexityCounter.reset();
        for (int i = 0; i < evictionsToTrigger; i++) {
            cache.put("overflow_key_" + i, "overflow_val_" + i);
        }
    }
}