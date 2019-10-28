package com.zifang.util.workflow.engine;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Map;

public abstract class AbstractEngineService {

    public abstract void setProperty(Map<String, String> properties);

    public abstract void exec();

    public abstract Dataset<Row> getDataset();
}
