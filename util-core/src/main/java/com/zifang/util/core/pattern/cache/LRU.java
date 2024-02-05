package com.zifang.util.core.pattern.cache;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Least Recently Used 淘汰策略
 */
public class LRU<K, V> extends LinkedHashMap<K, V> {

    // 保存缓存的容量
    private int capacity;

    public LRU(int capacity, float loadFactor) {
        super(capacity, loadFactor, true);
        this.capacity = capacity;
    }

    /**
     * 重写removeEldestEntry()方法设置何时移除旧元素
     *
     * @param eldest
     * @return
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        // 当元素个数大于了缓存的容量, 就移除元素
        return size() > this.capacity;
    }
}