package com.zifang.util.db.context;

import java.util.LinkedHashMap;
import java.util.Map;

public class DatasourceContextManager {

    public static final String DEFAULT = "default";

    /**
     * 持久层上下文 内部的所有的数据源上下文
     */
    private static final Map<String, DataSourceContext> dataSourceContextMap = new LinkedHashMap<>();

    /**
     * 注册管理器
     */
    public static void register(String dataSourceContextName, DataSourceContext dataSourceFactory) {
        assert dataSourceContextName != null && !"".equals(dataSourceContextName);
        if (dataSourceContextMap.get(dataSourceContextName) != null) {
            throw new RuntimeException("重复注册数据源上下文:key=" + dataSourceContextName);
        }
        dataSourceContextMap.put(dataSourceContextName, dataSourceFactory);
    }

    public static DataSourceContext fetchContext(String dataSourceContextName) {
        return dataSourceContextMap.get(dataSourceContextName);
    }
}
