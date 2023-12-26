package com.zifang.util.db.sync;

import com.zifang.util.db.context.DataSourceManager;

import javax.sql.DataSource;

public class SyncHelper {

    public static void main(String[] args) {

        DataSource dataSource = DataSourceManager.registerDataSource(
                "aaa",
                "jdbc:mysql://rm-bp11g0550d7oq42p9.mysql.rds.aliyuncs.comuseUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&useInformationSchema=true",
                "3306",
                "mdb",
                "mdb",
                "Future1234"
        );




    }

}
