package com.zifang.util.core.cache;

public class CacheTest {

    @org.junit.Test
    public void test001(){

        // 缓存提供者
        CacheProvider cacheProvider = new MemoryCacheProvider();

        // 缓存端构建器
        CacheClientBuilder cacheClientBuilder = new CacheClientBuilder();
        cacheClientBuilder.setCacheProvider(cacheProvider);

        // 缓存终端
        CacheClient cacheClient = cacheClientBuilder.build();

        cacheClient.set("a","b");
        Object o = cacheClient.get("a");
    }
}
