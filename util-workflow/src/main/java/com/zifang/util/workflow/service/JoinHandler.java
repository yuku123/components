package com.zifang.util.workflow.service;

import com.zifang.util.bigdata.spark.context.SparkContextFactory;
import com.zifang.util.bigdata.spark.mock.SparkDataMockUtil;
import com.zifang.util.workflow.interfaces.AbstractEngineService;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Map;

public class JoinHandler extends AbstractEngineService {


    Map<String, String> properties;
    private Dataset<Row> dataset;


    @Override
    public void setProperty(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public void exec() {
        dataset = SparkContextFactory.getLocalSparkContext().getSqlContext().sql(properties.get("sql"));
        dataset.show();
    }

    @Override
    public Dataset<Row> getDataset() {
        return dataset;
    }
}
