package com.zifang.util.core.demo.thirdpart.components.database.oracle;

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DruidUtil {

    private static DruidDataSource dataSource = null;
    private static String driverClassName = "oracle.jdbc.driver.OracleDriver";
    private static String userName = "***";
    private static String password = "***";
    private static String url = "jdbc:oracle:thin:@192.168.0.159:1521:ORCL";

    public static DruidDataSource getDbConnect() {
        try {
            if (dataSource == null) {
                dataSource = new DruidDataSource();
                //设置连接参数
                dataSource.setUrl(url);
                dataSource.setDriverClassName(driverClassName);
                dataSource.setUsername(userName);
                dataSource.setPassword(password);
                //配置初始化大小、最小、最大
                dataSource.setInitialSize(20);
                dataSource.setMinIdle(10);
                dataSource.setMaxActive(50);
                //连接泄漏监测
                dataSource.setRemoveAbandoned(true);
                dataSource.setRemoveAbandonedTimeout(30);
                //配置获取连接等待超时的时间
                dataSource.setMaxWait(20000);
                //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
                dataSource.setTimeBetweenEvictionRunsMillis(20000);
                //防止过期
                dataSource.setValidationQuery("select 1 from dual");
                dataSource.setTestWhileIdle(true);
                dataSource.setTestOnBorrow(true);
                dataSource.setLogAbandoned(true);
                dataSource.setKeepAlive(true);

                dataSource.setFilters("stat");
            } else {
                return dataSource;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = getDbConnect().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}