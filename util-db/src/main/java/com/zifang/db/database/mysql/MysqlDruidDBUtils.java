package com.zifang.db.database.mysql;

import com.alibaba.druid.pool.DruidDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlDruidDBUtils {
    private static DruidDataSource dataSource = null;

    /**
     * Get the database pool
     *
     * @return ComboPooledDataSource
     * @throws PropertyVetoException
     */
    public static DruidDataSource getComboPooledDataSource() {

        try{
            if(dataSource==null){
                dataSource=new DruidDataSource();
                //设置连接参数
                dataSource.setUrl("jdbc:mysql://localhost:3306/test?useSSL=false&autoReconnect=true");
                dataSource.setDriverClassName("com.mysql.jdbc.Driver");
                dataSource.setUsername("root");
                dataSource.setPassword("zxc123");
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
            }else {
                return dataSource;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return dataSource;
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = getComboPooledDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    
    public static void test() throws SQLException{
    	Connection connection = getConnection();
    	Statement statement = connection.createStatement();
    	ResultSet resultSet = statement.executeQuery("desc test.users");
    	while(resultSet.next()){
    		System.out.println(resultSet.getString(1));
    	}
    }
    
    public static void main(String[] args) throws SQLException {
        test();
	}

}
