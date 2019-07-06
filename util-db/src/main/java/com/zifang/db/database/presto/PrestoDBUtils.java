package com.zifang.db.database.presto;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;


public class PrestoDBUtils {
    private static String driverClassName = "com.facebook.presto.jdbc.PrestoDriver";
    private static String userName = "piday";
    private static String url = "jdbc:presto://192.168.1.103:9002/hive";
    private static ComboPooledDataSource dpds = null;

    public static ComboPooledDataSource getComboPooledDataSource() throws PropertyVetoException {
        if (dpds == null) {
            dpds = new ComboPooledDataSource();
            dpds.setDriverClass(driverClassName);
            dpds.setJdbcUrl(url);
            dpds.setUser(userName);
            dpds.setMinPoolSize(2);
            dpds.setInitialPoolSize(10);
            dpds.setMaxStatements(180);
            return dpds;
        } else return dpds;
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = getComboPooledDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return conn;
    }

}
