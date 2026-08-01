package com.example.lfucache.controller;

import com.example.lfucache.cache.CacheMetrics;
import com.example.lfucache.service.CacheService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cache")
public class CacheController {

    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping("/{key}")
    public ResponseEntity<String> get(@PathVariable String key) {
        String value = cacheService.get(key);
        if (value == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(value);
    }

    @PostMapping
    public ResponseEntity<Void> put(@RequestParam String key, @RequestParam String value) {
        cacheService.put(key, value);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/metrics")
    public ResponseEntity<CacheMetrics> getMetrics() {
        return ResponseEntity.ok(cacheService.getMetrics());
    }
}