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
    @DisplayName("Assert O(1) Complexity Deterministically: Eviction step count must not scale with cache capacity N")
    void testO1TimeComplexityDeterministically() {
        int smallCapacity = 10;
        int largeCapacity = 1000;
        int evictionsToPerform = 50;

        // 1. Measure inspections for Small Cache (N = 10)
        ComplexityCounter.reset();
        LfuCache<String, String> smallCache = new LfuCache<>(smallCapacity);
        runEvictionWorkload(smallCache, smallCapacity, evictionsToPerform);
        long opsSmallN = ComplexityCounter.get();

        // 2. Measure inspections for Large Cache (N = 1000)
        ComplexityCounter.reset();
        LfuCache<String, String> largeCache = new LfuCache<>(largeCapacity);
        runEvictionWorkload(largeCache, largeCapacity, evictionsToPerform);
        long opsLargeN = ComplexityCounter.get();

        System.out.printf("[O(1) Step Check] Small Cache (N=%d, %d evictions) -> %d inspections%n",
                smallCapacity, evictionsToPerform, opsSmallN);
        System.out.printf("[O(1) Step Check] Large Cache (N=%d, %d evictions) -> %d inspections%n",
                largeCapacity, evictionsToPerform, opsLargeN);

        // Verification guard: Ensure counter is actually recording operations!
        if (opsSmallN == 0 || opsLargeN == 0) {
            fail("Instrumentation error: Counter recorded 0 operations! Make sure ComplexityCounter.increment() is inside evict().");
        }

        if (opsSmallN != opsLargeN) {
            fail(String.format(
                    "FAILED O(1) Complexity Check! Evicting %d items from small capacity (N=%d) required %d element inspections, " +
                            "while large capacity (N=%d) required %d element inspections. " +
                            "Your eviction logic scales as O(N) instead of constant O(1).",
                    evictionsToPerform, smallCapacity, opsSmallN, largeCapacity, opsLargeN
            ));
        }

        assertEquals(opsSmallN, opsLargeN, "Operation step count must be equal across capacities for O(1) time complexity.");
    }

    private void runEvictionWorkload(LfuCache<String, String> cache, int capacity, int evictionsToTrigger) {
        // Fill cache to capacity
        for (int i = 0; i < capacity; i++) {
            cache.put("warmup_key_" + i, "val_" + i);
        }

        // Reset right before eviction phase
        ComplexityCounter.reset();

        // Trigger evictions
        for (int i = 0; i < evictionsToTrigger; i++) {
            cache.put("overflow_key_" + i, "overflow_val_" + i);
        }
    }
}