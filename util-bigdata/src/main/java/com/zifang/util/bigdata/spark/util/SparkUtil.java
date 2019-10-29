package com.zifang.util.bigdata.spark.util;

import com.zifang.util.bigdata.spark.context.SparkContextInstance;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

public class SparkUtil {

    private SparkContextInstance sparkContextInstance;

    public SparkUtil(SparkContextInstance sparkContextInstance){
        this.sparkContextInstance = sparkContextInstance;
    }
    /**
     * 传入JavaSparkContext 与文件路径 返回Dataset schema都是string的格式
     *
     * @param fileLocation 单个本地文件
     */

    public Dataset<Row> creatDataset(String fileLocation){

        JavaRDD<String> lineRDD = sparkContextInstance.getJavaSparkContext().textFile(fileLocation);

        String header = lineRDD.first();

        //把第一行拿掉
        //把string转化为row形式
        JavaRDD<Row> rows = lineRDD
                .filter(row -> !row.equals(header))
                .map(s-> RowFactory.create(s.split(",")));

        StructType schema = produceSchema(header);

        return sparkContextInstance.getSqlContext().createDataFrame(rows, schema);
    }

    private StructType produceSchema(String header) {
        StructType schema = new StructType();
        for(String item : header.split(",")){
            schema = schema.add(item, DataTypes.StringType);
        }
        return schema;
    }
}
