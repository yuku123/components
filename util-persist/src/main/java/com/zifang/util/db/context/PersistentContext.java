package com.zifang.util.db.context;

import java.util.LinkedHashMap;
import java.util.Map;

public class PersistentContext {

    public static final String DEFAULT = "default";

    /**
     * 持久层上下文 内部的所有的数据源上下文
     */
    private static Map<String, DataSourceContext> dataSourceContextMap = new LinkedHashMap<>();

    /**
     * 注册管理器
     * */
    public static void registerDatasourceContext(String key,DataSourceContext dataSourceFactory) {
        assert key != null && !"".equals(key);
        dataSourceContextMap.put(key,dataSourceFactory);
    }
}
