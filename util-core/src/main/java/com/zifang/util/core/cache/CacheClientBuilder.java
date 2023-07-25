package com.zifang.util.core.cache;

import lombok.Data;

@Data
public class CacheClientBuilder {

    private CacheProvider cacheProvider;
    private String dbNum;

    public CacheClient build() {
        CacheClient cacheClient = new DefaultCacheClient();
        return cacheClient;
    }
}
