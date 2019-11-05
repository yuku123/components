package com.zifang.util.bigdata.zztest;

import com.zifang.util.bigdata.spark.context.SparkContextInstance;
import com.zifang.util.bigdata.spark.util.SparkUtil;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.StructField;

import java.util.ArrayList;
import java.util.List;

public class PivotTest {
    public static void main(String[] args) {
        SparkContextInstance sparkContextInstance = new SparkContextInstance();
        Dataset<Row> dataset = new SparkUtil(sparkContextInstance).createDataSet("file:/home/zifang/workplace/idea_workplace/components/util-bigdata/src/test/resources/input1.csv");

        dataset.show();
        for(StructField field :dataset.schema().fields()){
            System.out.println(field.name()+":"+field.dataType().typeName());
        }

        List<Object> a = new ArrayList<Object>();
        a.add("name_u1");
        a.add("name_u2");
        a.add("name_u3");
        a.add("name_u4");
        a.add("name_u5");

        dataset.groupBy(dataset.col("userId"))
                .pivot("name",a)
                .agg(functions.sum("value"))
                .na().fill(0).show();
        dataset.groupBy("userId")
                .pivot("name",a)
                .count()
                .na().fill(0).show();
    }
}
