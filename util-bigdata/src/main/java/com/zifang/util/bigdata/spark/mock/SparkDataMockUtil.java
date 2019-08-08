package com.zifang.util.bigdata.spark.mock;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

public class SparkDataMockUtil implements java.io.Serializable{

    public Dataset<Row> creatDataset(JavaSparkContext sc,String fileLocation){

        SQLContext sqlContext = new SQLContext(sc);

        JavaRDD<String> lineRDD = sc.textFile(fileLocation);

        String header = lineRDD.first();

        //把第一行拿掉
        //把string转化为row形式
        JavaRDD<Row> rows = lineRDD
                .filter(row -> !row.equals(header))
                .map(s->RowFactory.create(s.split(",")));

        StructType schema = produceSchema(header);

        Dataset<Row> nrows = sqlContext.createDataFrame(rows, schema);

        return nrows;
    }

    private StructType produceSchema(String header) {
        StructType schema = new StructType();
        for(String item : header.split(",")){
            schema = schema.add(item, DataTypes.StringType);
        }
        return schema;
    }

    public static void main(String[] args) {
        String fileLocation = "/Users/zifang/workplace/idea_workplace/components/util-bigdata/src/main/resources/test.txt";
        SparkConf conf = new SparkConf();
        conf.setMaster("local").setAppName("RDD");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SparkDataMockUtil sparkDataMockUtil = new SparkDataMockUtil();
        Dataset<Row> rowDataset = sparkDataMockUtil.creatDataset(sc,fileLocation);
        rowDataset.show();
    }
}