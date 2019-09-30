package com.zifang.util.praser.xml.example.annotations;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Test {
    public static void main(String[] args) throws SQLException {
        Connection connection = null;

        Statement statement  = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);

        statement.executeUpdate("this is sql",Statement.RETURN_GENERATED_KEYS);

        ResultSet resultSet = statement.getGeneratedKeys();

        if(resultSet.next()){
            resultSet.getInt(1);
        }

    }
}
