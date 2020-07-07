package com.zifang.util.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.zifang.util.db.context.DataSourceContext;
import com.zifang.util.db.context.PersistentContext;
import com.zifang.util.db.transation.TransationManager;
import org.junit.Test;
import com.zifang.util.core.collection.Lists;
import javax.sql.DataSource;


public class BasicTest {

    DruidDataSource dataSource = null;
    @Test
    public void test(){

        MysqlDatasourceFactory mysqlDatasourceFactory = new MysqlDatasourceFactory();


        // 持久层上下文
        PersistentContext.setDataSourceContexts(
                Lists.of(
                         new DataSourceContext()
                                .scanPackage("aaa.bbb")
                                .transationManager(new TransationManager()) // 事务管理器
                                .dataSourceFactory(mysqlDatasourceFactory)//设置数据源
                        ,new DataSourceContext()
                                .scanPackage("aaa.bbb")
                                .transationManager(new TransationManager()) // 事务管理器
                                .dataSourceFactory(mysqlDatasourceFactory)//设置数据源
                )
        );


    }
}
