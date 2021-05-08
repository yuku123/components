package com.zifang.util.bigdata.spark.util;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

/**
 * 使用local的方式，导入标准格式的数据集，返回模拟的Dataset出来
 */
public class SparkDataMockUtil implements java.io.Serializable {

    /**
     * 传入JavaSparkContext 与文件路径 返回Dataset schema都是string的格式
     *
     * @param sc           JavaSparkContext类型的上下文
     * @param fileLocation 单个本地文件
     */

    public Dataset<Row> creatDataset(JavaSparkContext sc, String fileLocation) {

        SQLContext sqlContext = new SQLContext(sc);

        JavaRDD<String> lineRDD = sc.textFile(fileLocation);

        String header = lineRDD.first();

        //把第一行拿掉
        //把string转化为row形式
        JavaRDD<Row> rows = lineRDD
                .filter(row -> !row.equals(header))
                .map(s -> RowFactory.create(s.split(",")));

        StructType schema = produceSchema(header);

        Dataset<Row> nrows = sqlContext.createDataFrame(rows, schema);

        return nrows;
    }

    private StructType produceSchema(String header) {
        StructType schema = new StructType();
        for (String item : header.split(",")) {
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
        Dataset<Row> rowDataset = sparkDataMockUtil.creatDataset(sc, fileLocation);
        rowDataset.show(20);
    }
}