package com.zifang.util.workflow.engine.spark.services;

import com.zifang.util.workflow.config.ExecutableWorkflowNode;
import com.zifang.util.workflow.engine.interfaces.AbstractEngineService;
import com.zifang.util.workflow.engine.spark.impl.AbstractSparkEngineService;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Map;

public class EmptyHandler extends AbstractSparkEngineService {

    @Override
    public void setProperty(Map<String, String> properties) {

    }

    @Override
    public void exec(ExecutableWorkflowNode executableWorkflowNode) {

    }

    @Override
    public Dataset<Row> getDataset() {
        return null;
    }
}
