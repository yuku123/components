package com.zifang.util.bigdata.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import java.io.IOException;
import java.io.Serializable;

public class Test2 implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	transient SparkConf conf = new SparkConf().setAppName("Spark WordCount written by Java").setMaster("local");
	transient JavaSparkContext sc = new JavaSparkContext(conf); // ��ײ�ʵ���Ͼ���Scala��SparkContext
	
	public void f(String f) {
		JavaRDD<String> lines = sc.textFile("aa.txt");
		JavaRDD<String> result = lines.map(new Function<String, String>() {
			@Override
			public String call(String v1) throws Exception {
				return "a";
			}
		});
		result.saveAsTextFile(f);
		//sc.stop();
	}
	
	public static void main(String[] args) throws IOException {
		Test2 test2 = new Test2();
		test2.f("aa");
		test2.f("bb");
		test2.f("cc");
	}
}
