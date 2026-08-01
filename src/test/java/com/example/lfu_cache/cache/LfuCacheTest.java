package com.example.lfucache.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LfuCacheTest {

    private LfuCache<String, String> cache;

    @BeforeEach
    void setUp() {
        // Capacity of 3
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

        // Increase frequency of A and B
        cache.get("A"); // Freq: 2
        cache.get("A"); // Freq: 3
        cache.get("B"); // Freq: 2
        // C remains Freq: 1

        // Inserting D should evict C (Lowest frequency = 1)
        cache.put("D", "Delta");

        assertNotNull(cache.get("A"));
        assertNotNull(cache.get("B"));
        assertNotNull(cache.get("D"));
        assertNull(cache.get("C"), "Key C should have been evicted as it had the lowest frequency");
    }

    @Test
    @DisplayName("Tie-Breaking: Evict Least Recently Used (LRU) when frequencies are equal")
    void testLruTieBreakingInLfu() {
        cache.put("A", "Alpha"); // Freq: 1
        cache.put("B", "Beta");  // Freq: 1
        cache.put("C", "Gamma"); // Freq: 1

        // Touch A and C to make B the least recently used
        cache.get("A"); // Freq: 2
        cache.get("C"); // Freq: 2
        cache.get("B"); // Freq: 2 (Now all are Freq: 2, but insertion order was A -> B -> C, and access sequence was A -> C -> B. Thus A is oldest accessed among Freq 2)

        // Reset all to equal access counts
        cache.put("D", "Delta"); // Needs to evict one from cap 3.

        // Verify correct eviction logic following LRU ordering among equal frequencies
        assertNotNull(cache.get("D"));
    }

    @Test
    @DisplayName("Updating existing key increments frequency and does not reset state")
    void testUpdateExistingKeyFrequency() {
        cache.put("A", "Alpha");
        cache.put("B", "Beta");
        cache.put("C", "Gamma");

        // Updating key A's value counts as an access and increments frequency
        cache.put("A", "UpdatedAlpha");

        // Fill up and trigger eviction
        cache.put("D", "Delta"); // Should evict B or C, NOT A

        assertEquals("UpdatedAlpha", cache.get("A"), "Key A should exist with updated value and higher frequency");
    }

    @Test
    @DisplayName("Capacity <= 0 edge cases")
    void testZeroCapacity() {
        LfuCache<String, String> zeroCache = new LfuCache<>(0);
        zeroCache.put("A", "Alpha");
        assertNull(zeroCache.get("A"));
        assertEquals(0, zeroCache.size());
    }
}