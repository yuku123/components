package com.zifang.util.core.collections;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlSelectParser;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.DoubleStream;

public class MemorySqlExcutor {

    private Map<String,List<?>> dataMap = new LinkedHashMap<>();

    public MemorySqlExcutor setData(List<Map<String, Object>> data, String alias) {

        dataMap.put(alias,data);

        return this;
    }

    public MemorySqlExcutor select(String sql) {

        MySqlStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement statement = parser.parseStatement();

        SQLSelectStatement sqlSelectStatement = (SQLSelectStatement)statement;
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        sqlSelectStatement.accept(visitor);
        return this;
    }
}