package com.zifang.util.core.pattern.cache;

/**
 * 提供一个cache
 *
 * @author zifang
 */
public interface CacheClient {

    Object get(String key);

    void set(String key, Object value);
}