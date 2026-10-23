package com.example.lfu_cache.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LfuCacheTest {

    private Cache<String, String> cache;

    @BeforeEach
    void setUp() {
        cache = new LfuCache<>(3);
    }

    @Test
    @DisplayName("Basic Put and Get operations")
    void testBasicPutAndGet() {
        cache.put("A", "Alpha");
        cache.put("B", "Beta");

        assertEquals("Alpha", cache.get("A"));
        assertEquals("Beta", cache.get("B"));
        assertNull(cache.get("C"));
    }

    @Test
    @DisplayName("Eviction based strictly on Lowest Frequency (LFU)")
    void testLfuEviction() {
        cache.put("A", "Alpha");
        cache.put("B", "Beta");
        cache.put("C", "Gamma");

        cache.get("A");
        cache.get("A");
        cache.get("B");

        cache.put("D", "Delta");

        assertNotNull(cache.get("A"));
        assertNotNull(cache.get("B"));
        assertNotNull(cache.get("D"));
        assertNull(cache.get("C"));
    }

    @Test
    @DisplayName("Zero capacity edge case")
    void testZeroCapacity() {
        Cache<String, String> zeroCache = new LfuCache<>(0);
        zeroCache.put("A", "Alpha");
        assertNull(zeroCache.get("A"));
        assertEquals(0, zeroCache.size());
    }
}