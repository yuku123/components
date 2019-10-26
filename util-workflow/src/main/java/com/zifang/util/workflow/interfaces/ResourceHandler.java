package com.zifang.util.workflow.interfaces;

import com.zifang.util.bigdata.spark.context.SparkContextFactory;
import com.zifang.util.bigdata.spark.mock.SparkDataMockUtil;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Map;

public class ResourceHandler extends AbstractEngineService{

    private Dataset<Row> dataset;
    private Map<String, String> properties;

    @Override
    public void setProperty(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public void exec() {
        dataset = new SparkDataMockUtil(SparkContextFactory.getLocalSparkContext()).creatDataset(properties.get("localFile"));
    }

    @Override
    public Dataset<Row> getDataset() {
        return dataset;
    }

    public static void main(String[] args) {
        String path = "util-workflow/src/main/resources/input1.csv";
        Dataset<Row> dataset = new SparkDataMockUtil(SparkContextFactory.getLocalSparkContext()).creatDataset(path);
        dataset.show();
    }
}
