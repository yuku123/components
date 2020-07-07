package com.zifang.util.db.context;

import com.zifang.util.db.transation.TransationManager;

public class DataSourceContext {

    private DatasourceFactory datasourceFactory;

    private String scanPackageName;

    private TransationManager transationManager;


    public DataSourceContext dataSourceFactory(DatasourceFactory datasourceFactory) {
        this.datasourceFactory = datasourceFactory;
        return this;
    }

    public DataSourceContext scanPackage(String scanPackageName) {
        this.scanPackageName = scanPackageName;
        return this;
    }

    public DataSourceContext transationManager(TransationManager transationManager) {
        this.transationManager = transationManager;
        return this;
    }
}
