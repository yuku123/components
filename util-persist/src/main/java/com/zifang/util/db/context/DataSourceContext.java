package com.zifang.util.db.context;

import com.zifang.util.db.transation.TranslationManager;
import lombok.Data;

@Data
public class DataSourceContext {

    private DatasourceFactory datasourceFactory;

    private String scanPackageName;

    private TranslationManager transactionManager;


    public DataSourceContext dataSourceFactory(DatasourceFactory datasourceFactory) {
        this.datasourceFactory = datasourceFactory;
        return this;
    }

    public DataSourceContext scanPackage(String scanPackageName) {
        this.scanPackageName = scanPackageName;
        return this;
    }

    public DataSourceContext transationManager(TranslationManager transationManager) {
        this.transactionManager = transationManager;
        return this;
    }
}
