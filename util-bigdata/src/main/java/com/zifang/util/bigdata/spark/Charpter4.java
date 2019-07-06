package com.zifang.util.bigdata.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.Arrays;

public class Charpter4 {

	public static SparkConf conf = new SparkConf().setAppName("Spark WordCount written by Java").setMaster("local");
	public static JavaSparkContext sc = new JavaSparkContext(conf);

	public static void main(String[] args) {

		JavaRDD<String> rdd = sc.parallelize(Arrays.asList("aa bb cc", "dd", "ee", "hellow ff"));

		JavaRDD<String> lines = sc.textFile("aa.txt");

		PairFunction<String, String, String> keyData = new PairFunction<String, String, String>() {
			public Tuple2<String, String> call(String x) {
				return new Tuple2(x.split(" ")[0], x);
			}
		};
		JavaPairRDD<String, String> pairs = lines.mapToPair(keyData);
		System.out.println(pairs.collect());

		Function<Tuple2<String, String>, Boolean> longWordFilter = new Function<Tuple2<String, String>, Boolean>() {
			public Boolean call(Tuple2<String, String> keyValue) {
				return (keyValue._2().length() < 10);
			}
		};
		JavaPairRDD<String, String> result = pairs.filter(longWordFilter);
		System.out.println(result.collect());
	}

}
