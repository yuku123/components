package com.zifang.util.bigdata.spark.context;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

public class SparkSessionLoader {

	private static SparkSessionLoader instance = null;
	private static SparkSession session = null;
	private static SparkContext ctx = null;
	private static JavaSparkContext jctx = null;
	private static SparkConf conf = null;

	public static SparkSessionLoader getInstance(String master, String app) {
		if(null == instance) {
			synchronized (SparkSessionLoader.class) {
				if(null == instance) {
					instance = new SparkSessionLoader();
					conf = new SparkConf().setMaster(master).setAppName(app)
							.setJars(new String[] {"common.jar"})
							.set("com.zifang.util.bigdata.spark.sql.warehouse.dir", "hive.warehouse")
							.set("hive.metastore.uris", "hive.metastore.uris")
							.set("com.zifang.util.bigdata.spark.executor.memory","com.zifang.util.bigdata.spark.executor.memory")
							.set("com.zifang.util.bigdata.spark.driver.maxResultSize", "com.zifang.util.bigdata.spark.driver.maxResultSize")
							.set("com.zifang.util.bigdata.spark.cores.max", "com.zifang.util.bigdata.spark.cores.max");

					session = SparkSession.builder()
							.appName(app)
							.master(master)
						    .config(conf)
							.enableHiveSupport()
							.getOrCreate();
					ctx = session.sparkContext();
					ctx.setLogLevel("ERROR");
					jctx = JavaSparkContext.fromSparkContext(ctx);
				}
			}
		}
		return instance;
	}
	
	public SparkSession getSession() {
		return session;
	}
	
	public SparkContext getContext() {
		return ctx;
	}
	public JavaSparkContext getJavaContext() {
		return jctx;
	}
}
