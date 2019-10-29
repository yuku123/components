package com.zifang.util.workflow.engine.interfaces;

import com.zifang.util.workflow.config.ExecutableWorkflowNode;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Map;

public abstract class AbstractEngineService {

    protected Map<String, String> properties;

    protected Dataset<Row> dataset;

    public void setProperty(Map<String, String> properties) {
        this.properties = properties;
    }

    public abstract void exec(ExecutableWorkflowNode executableWorkflowNode);


    public Dataset<Row> getDataset() {
        return dataset;
    }
}
