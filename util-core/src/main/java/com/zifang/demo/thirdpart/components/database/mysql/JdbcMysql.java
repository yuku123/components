package com.zifang.demo.thirdpart.components.database.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcMysql {
    private Connection connection;
    private Statement statement;

    public JdbcMysql() throws ClassNotFoundException, SQLException {
        connection = MysqlC3P0DBUtils.getConnection();
        statement = connection.createStatement();
    }

    public ResultSet select(String sql) throws SQLException {
        return statement.executeQuery(sql);
    }

    public boolean exec(String query) throws SQLException {
        return statement.execute(query);
    }
    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() throws SQLException {
        statement.close();
        connection.close();
    }

}
