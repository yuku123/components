package com.zifang.util.core.demo.thirdpart.components.database.presto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcPresto {
    private Connection connection;

    private Statement statement;

    public JdbcPresto() throws ClassNotFoundException, SQLException {
        connection = PrestoDBUtils.getConnection();
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

    public Statement getStatement() {
        return statement;
    }
}
