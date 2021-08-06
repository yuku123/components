package com.zifang.util.db.respository;

import lombok.Data;

import java.util.Map;

@Data
public class BoundSql {

    private String originSql;

    private String transformSql;

    private Map<Integer,String> indexName;

    private Map<Integer,Object> indexValue;

    private Map<Integer,Object> indexValueInsert;
}
