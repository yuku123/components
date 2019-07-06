//package com.zifang.util.bigdata.spark;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.spark.SparkConf;
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.api.java.function.FlatMapFunction;
//import org.apache.spark.api.java.function.Function;
//import org.apache.spark.api.java.function.Function2;
//
//import java.util.Arrays;
//import java.util.Iterator;
//
//public class Test {
//
//	public static SparkConf conf = new SparkConf().setAppName("Spark WordCount written by Java").setMaster("local");
//	public static JavaSparkContext sc = new JavaSparkContext(conf); // ��ײ�ʵ���Ͼ���Scala��SparkContext
//
//	public static void test1_map() {
//		JavaRDD<Integer> rdd = sc.parallelize(Arrays.asList(1, 2, 3, 4,3));
//		JavaRDD<String> result = rdd.map(new Function<Integer, String>() {
//			@Override
//			public String call(Integer v1) throws Exception {
//				return String.valueOf(v1 * v1);
//			}
//		});
//		System.out.println(StringUtils.join(result.collect(), ","));
//
//		Integer sum = rdd.reduce(new Function2<Integer, Integer, Integer>() {
//		       public Integer call(Integer x, Integer y) { return x + y; }
//		});
//
//		System.out.println(sum);
//	}
//
//	public static void test2_flatmap() {
//		JavaRDD<String> rdd = sc.parallelize(Arrays.asList("aa bb cc", "dd", "ee", "hellow ff"));
//		JavaRDD<String> words = rdd.flatMap(new FlatMapFunction<String, String>() {
//			@Override
//			public Iterator<String> call(String t) throws Exception {
//				return Arrays.asList(t.split(" ")).iterator();
//			}
//		});
//		JavaRDD<String> errorsRDD = rdd.filter(new Function<String, Boolean>() {
//			public Boolean call(String x) {
//				return x.contains("aa");
//			}
//		});
//	}
//
//
//	public static void avgTest() {
//		JavaRDD<Integer> rdd = sc.parallelize(Arrays.asList(1, 2, 3, 4,3));
//
//		Function2<AvgCount, Integer, AvgCount> addAndCount = new Function2<AvgCount, Integer, AvgCount>() {
//			public AvgCount call(AvgCount a, Integer x) {
//				a.total += x;
//				a.num += 1;
//				return a;
//			}
//		};
//		Function2<AvgCount, AvgCount, AvgCount> combine = new Function2<AvgCount, AvgCount, AvgCount>() {
//			public AvgCount call(AvgCount a, AvgCount b) {
//				a.total += b.total;
//				a.num += b.num;
//				return a;
//			}
//		};
//		AvgCount initial = new AvgCount(0, 0);
//		AvgCount result_a = rdd.aggregate(initial, addAndCount, combine);
//		System.out.println(result_a.avg());
//		System.out.println(rdd.countByValue());
//
//
//	}
//
//	public static void main(String[] args) {
//		//test1_map();
//		//test2_flatmap();
//		avgTest();
//	}
//
//}