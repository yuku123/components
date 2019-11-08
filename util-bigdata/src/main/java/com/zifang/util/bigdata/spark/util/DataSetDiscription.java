package com.zifang.util.bigdata.spark.util;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructField;

import java.util.ArrayList;
import java.util.List;

/**
 * 提供对这个dataSet的全方位的统计量
 * */
public class DataSetDiscription {

    private Dataset<Row> dataset;

    public DataSetDiscription(Dataset<Row> dataset){
        this.dataset = dataset;
    }

    /**
     *
     * */
    public List<String> getColumnList(){
        List<String> columnList = new ArrayList<>();
        for(StructField field :dataset.schema().fields()){
            columnList.add(field.name());
        }
        return columnList;
    }
}
