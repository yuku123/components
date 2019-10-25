package com.zifang.util.bigdata.spark.mock;

import com.zifang.util.bigdata.spark.context.LocalSparkContext;
import com.zifang.util.bigdata.spark.context.SparkContextFactory;
import com.zifang.util.bigdata.spark.udf.TransformToPoint;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.junit.Test;

public class SparkDataMockUtilTest {

    @Test
    public void creatDataset() {
        String fileLocation = "/Users/zifang/workplace/idea_workplace/components/util-bigdata/src/main/resources/test.txt";
        LocalSparkContext localSparkContext = SparkContextFactory.getLocalSparkContext();
        SparkDataMockUtil sparkDataMockUtil = new SparkDataMockUtil(localSparkContext);
        Dataset<Row> rowDataset = sparkDataMockUtil.creatDataset(fileLocation);
        rowDataset.registerTempTable("aa");
        SQLContext sqlContext = localSparkContext.getSqlContext();
        sqlContext.udf().register("a",new TransformToPoint(), DataTypes.IntegerType);
        Dataset<Row> aa = sqlContext.sql("select *,a('2001-11-11','month') as time ,row_number() over (order by 1) as index from aa");
        aa.show();
        rowDataset.show();
    }
}