package com.zifang.util.bigdata.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class JavaSparkContextSingleton {
	
	private static SparkConf conf = new SparkConf().setAppName("Spark WordCount").setMaster("local");
	private static JavaSparkContext sc = new JavaSparkContext(conf);

	public static JavaSparkContext getSparkContextInstance() {
		return sc;
	}

}
