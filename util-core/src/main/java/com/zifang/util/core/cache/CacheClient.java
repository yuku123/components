package com.zifang.util.core.cache;

/**
 * 提供一个类似redis的内存cache
 *
 * @author zifang
 */
public interface CacheClient {
    Object get(String key);

    void set(String key, Object value);
}