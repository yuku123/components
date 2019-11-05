package com.zifang.util.workflow.engine.spark.services.praser;

import java.util.List;
import java.util.Map;

public class PivotA {
    /**
     * 对需要进行pivot处理的列的所有定义
     * */
    List<PivotANode> pivotColumnDefinations;
    Map<String,String> columnMap;

    public List<PivotANode> getPivotColumnDefinations() {
        return pivotColumnDefinations;
    }

    public void setPivotColumnDefinations(List<PivotANode> pivotColumnDefinations) {
        this.pivotColumnDefinations = pivotColumnDefinations;
    }

    public Map<String, String> getColumnMap() {
        return columnMap;
    }

    public void setColumnMap(Map<String, String> columnMap) {
        this.columnMap = columnMap;
    }
}