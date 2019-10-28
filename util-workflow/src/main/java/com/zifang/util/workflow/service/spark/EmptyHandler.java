package com.zifang.util.workflow.service.spark;

import com.zifang.util.workflow.config.ExecutableWorkflowNode;
import com.zifang.util.workflow.engine.AbstractEngineService;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Map;

public class EmptyHandler extends AbstractEngineService {

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
