package com.zifang.util.db;

import com.zifang.util.core.collections.Lists;
import com.zifang.util.db.context.DataSourceContext;
import com.zifang.util.db.context.PersistentContext;
import com.zifang.util.db.entity1.User;
import com.zifang.util.db.respository.RepositoryProxy;
import com.zifang.util.db.transation.TransationManager;
import org.junit.Test;

import java.util.List;


public class BasicTest {

    @Test
    public void test() {

        MysqlDatasourceFactory mysqlDatasourceFactory = new MysqlDatasourceFactory();

        // 持久层上下文
        PersistentContext.setDataSourceContexts(
                Lists.of(
                        new DataSourceContext()
                                .scanPackage("aaa.bbb")
                                .transationManager(new TransationManager()) //事务管理器
                                .dataSourceFactory(mysqlDatasourceFactory)  //设置数据源
                        , new DataSourceContext()
                                .scanPackage("aaa.bbb")
                                .transationManager(new TransationManager()) //事务管理器
                                .dataSourceFactory(mysqlDatasourceFactory)  //设置数据源
                )
        );

        TestInterface testInterface = RepositoryProxy.proxy(TestInterface.class);

        List<User> userList = testInterface.findByName("aa");


    }
}
