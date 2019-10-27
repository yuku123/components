package com.zifang.util.workflow.service;

import com.zifang.util.workflow.interfaces.AbstractEngineService;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Map;

public class EmptyHandler extends AbstractEngineService {

    @Override
    public void setProperty(Map<String, String> properties) {

    }

    @Override
    public void exec() {

    }

    @Override
    public Dataset<Row> getDataset() {
        return null;
    }
}
