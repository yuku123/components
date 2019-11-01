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
     * 传入绝对路径
     *
     * @param fileLocation 单个本地文件
     */
    public Dataset<Row> createDataSet(String fileLocation){

        Dataset<Row> dataSet = sparkContextInstance.getSqlContext().read()
                .format("csv")
                .option("header","true")
                .load(fileLocation);
        return dataSet;
    }
}
