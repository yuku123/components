package com.zifang.util.db.database.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcDruidMysql {
    private Connection connection;
    private Statement statement;

    public JdbcDruidMysql() throws ClassNotFoundException, SQLException {
        connection = MysqlDruidDBUtils.getConnection();
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
