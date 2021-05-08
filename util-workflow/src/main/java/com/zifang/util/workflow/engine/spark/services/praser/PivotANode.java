package com.zifang.util.workflow.engine.spark.services.praser;

import java.util.List;

public class PivotANode {

    /**
     * 将要被处理的column列名字
     */
    private String columnName;

    /**
     * 定义这个列下面的哪些直需要被pivot,其他的全部被省略
     */
    private List<String> pivotColumns;

    /**
     * 统计频次的列
     */
    private String value;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public List<String> getPivotColumns() {
        return pivotColumns;
    }

    public void setPivotColumns(List<String> pivotColumns) {
        this.pivotColumns = pivotColumns;
    }
}
