package com.zifang.util.db.sync;

import com.zifang.util.db.context.DataSourceManager;

import javax.sql.DataSource;

public class SyncHelper {

    public static void main(String[] args) {

        DataSource dataSource = DataSourceManager.registerDataSource(
                "aaa",
                "rm-bp11g0550d7oq42p9.mysql.rds.aliyuncs.com",
                3306,
                "mdbdemo",
                "mdb",
                "Future1234"
        );

        SqlExecutor sqlExecutor = new SqlExecutor(dataSource);

        System.out.println(sqlExecutor.fetchTableInfo("mdb"));

    }

}
