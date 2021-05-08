package com.zifang.util.db.context;

import java.util.List;

public class PersistentContext {

    /**
     * 持久层上下文 内部的所有的数据源上下文
     */
    private static List<DataSourceContext> dataSourceContexts;

    public static void setDataSourceContexts(List<DataSourceContext> dataSourceContexts) {
        PersistentContext.dataSourceContexts = dataSourceContexts;
    }
}
