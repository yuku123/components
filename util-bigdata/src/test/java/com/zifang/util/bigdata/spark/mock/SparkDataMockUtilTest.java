package com.zifang.util.bigdata.spark.mock;

import com.zifang.util.bigdata.spark.context.LocalSparkContext;
import com.zifang.util.bigdata.spark.context.SparkContextFactory;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.junit.Test;

import static org.junit.Assert.*;

public class SparkDataMockUtilTest {

    @Test
    public void creatDataset() {
        String fileLocation = "/Users/zifang/workplace/idea_workplace/components/util-bigdata/src/main/resources/test.txt";
        LocalSparkContext localSparkContext = SparkContextFactory.getLocalSparkContext();
        SparkDataMockUtil sparkDataMockUtil = new SparkDataMockUtil(localSparkContext);
        Dataset<Row> rowDataset = sparkDataMockUtil.creatDataset(fileLocation);
        rowDataset.registerTempTable("aa");
        SQLContext sqlContext = localSparkContext.getSqlContext();
        Dataset<Row> aa = sqlContext.sql("select *,row_number() over (order by 1) as index from aa");
        aa.show();
        rowDataset.show();
    }
}