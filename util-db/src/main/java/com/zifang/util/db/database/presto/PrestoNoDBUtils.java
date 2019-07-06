package com.zifang.util.db.database.presto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PrestoNoDBUtils {
    private static String driverClassName = "com.facebook.presto.jdbc.PrestoDriver";
    private static String userName = "piday";
    private static String url = "jdbc:presto://192.168.1.103:9002/hive";

    public static Connection getConnection() {
        try {
            // 加载驱动
            Class.forName(driverClassName);
            // 设置 配置数据
            // 1.url(数据看服务器的ip地址 数据库服务端口号 数据库实例)
            // 2.user
            // 3.password
            Connection conn = DriverManager.getConnection(url, userName, null);
            // 开始连接数据库
            System.out.println("数据库连接成功..");
            return conn;
        } catch (ClassNotFoundException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        return null;
    }
}
