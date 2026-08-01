# High-Throughput LFU Cache System Specification

## Architecture Overview
This application serves as an in-memory Least Frequently Used (LFU) key-value cache built for high-throughput HTTP REST environments.

## Build & Execution (Gradle)
- **Build Project:** `./gradlew build`
- **Run Application:** `./gradlew bootRun`
- **Run Tests:** `./gradlew test`

## System Requirements & Technical Constraints

1. **Time Complexity Requirements:**
    - Both `GET` and `PUT` operations MUST run in strictly **O(1) average time complexity**.
    - Linear scans or collection recalculations on cache access are strictly forbidden.

2. **Tie-Breaking Rule (LRU Fallback):**
    - When multiple keys share the minimum frequency, the cache MUST evict the **Least Recently Used (LRU)** key among those minimum frequency keys.

3. **Concurrency & Thread Safety:**
    - The cache will be accessed by hundreds of parallel web request threads simultaneously.
    - Operations must be thread-safe without coarse-grained full-cache synchronized blocks that degrade throughput.

4. **Metrics Integrity:**
    - Hit rates and eviction counters MUST be exact and thread-safe.
    - Cache size queries must be accurate without incurring locks on cache reads.

5. **Frequency Increment Rule:**
    - Accessing an existing key via `GET` increments its frequency by 1.
    - Updating the value of an existing key via `PUT` counts as an access and MUST increment its frequency by 1.
    - Inserting a new key initializes its frequency to 1.
    - *Note:* If a key is updated, its frequency must carry over and increment; it must not reset.

## Known Architecture Deficiencies & Tasks
Refer to inline `TODO` tags throughout the codebase for specific technical debt and implementation guidelines.