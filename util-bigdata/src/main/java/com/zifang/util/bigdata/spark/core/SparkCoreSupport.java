package com.zifang.util.bigdata.spark.core;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;



/**
 *
 * JavaRDD<String>
 * FlatMapFunction
 * JavaRDD<String>
 * Function<Row, String>
 *   PairFunction<String, Integer, String>()
 * JavaPairRDD<Integer,String>
 *
 *
 *
 *
 *
 *
 * */
public class SparkCoreSupport {

    /***/
    public static Dataset<String> transform(Dataset<Row> dataset){
        JavaRDD<Row> javaRDD = dataset.javaRDD();
        return null;
    }

}
