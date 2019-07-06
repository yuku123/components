package com.zifang.util.core.demo.thirdpart.components.database.mysql;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MysqlC3P0DBUtils {
    private static String driverClassName = "com.mysql.jdbc.Driver";
    private static String userName = "piday";
    private static String password = "pidayOffice";
    private static String url = "jdbc:mysql://192.168.1.103:3306/jiangnan?useSSL=false&autoReconnect=true";
    private static ComboPooledDataSource dpds = null;

    public static ComboPooledDataSource getComboPooledDataSource() throws PropertyVetoException {
        if (dpds == null) {
            dpds = new ComboPooledDataSource();
            dpds.setDriverClass(driverClassName);
            dpds.setJdbcUrl(url);
            dpds.setUser(userName);
            dpds.setPassword(password);
            dpds.setMinPoolSize(2);
            dpds.setInitialPoolSize(10);
            dpds.setMaxStatements(180);
            dpds.setMaxIdleTime(25200);
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
    
    public static void test() throws SQLException{
    	Connection connection = getConnection();
    	Statement statement = connection.createStatement();
    	ResultSet resultSet = statement.executeQuery("desc hy_finance.user_status_churn");
    	while(resultSet.next()){
    		System.out.println(resultSet.getString(1));
    	}
    }
    
    public static void main(String[] args) throws SQLException {
    	test();
	}

}
