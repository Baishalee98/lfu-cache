package com.example.lfu_cache.cache;

public class FrequencyBucket<K, V> {
    private final CacheNode<K, V> head;
    private final CacheNode<K, V> tail;

    public FrequencyBucket() {
        head = new CacheNode<>(null, null);
        tail = new CacheNode<>(null, null);
        head.next = tail;
        tail.prev = head;
    }

    public void addToTail(CacheNode<K, V> node) {
        node.prev = tail.prev;
        node.next = tail;
        tail.prev.next = node;
        tail.prev = node;
    }

    public void remove(CacheNode<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    public boolean isEmpty() {
        return head.next == tail;
    }

    public CacheNode<K, V> getFirst() {
        return head.next == tail ? null : head.next;
    }
}