package com.zifang.util.bigdata.spark.context;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;

public class JavaContextLoader {

	public JavaSparkContext getContext(String master, String app) {
		SparkContext ctx = SparkSessionLoader.getInstance(master, app).getContext();
		return JavaSparkContext.fromSparkContext(ctx);
	}
}
