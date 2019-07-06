package com.zifang.demo.jdk.java.util.lambda;

import java.util.Arrays;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CollectionDemo {


	public static void mapLambda() {
		Map<String, Integer> items = new HashMap<>();
		items.put("A", 10);
		items.put("B", 20);
		items.put("C", 30);
		items.put("D", 40);
		items.put("E", 50);
		items.put("F", 60);

		items.forEach((k, v) -> System.out.println("Item : " + k + " Count : " + v));

		items.forEach((k, v) -> {
			if ("E".equals(k)) {
				System.out.println(k + ":" + v);
			}
		});
	}


	public static void listLambda() {
		List<String> items = Arrays.asList("A", "B", "C", "D", "E");

		// lambda
		// Output : A,B,C,D,E
		items.forEach(item -> System.out.println(item));

		// Output : C
		items.forEach(item -> {
			if ("C".equals(item)) {
				System.out.println(item);
			}
		});
		items.forEach(System.out::println);

		items.stream().filter(s -> s.contains("B")).forEach(System.out::println);
	}

	/**
	 * 计算List中的元素的最大值，最小值，总和及平均值 @throws
	 */
	public static void listSummaryStatistics() {
		List<Integer> primes = Arrays.asList(2, 3, 5, 7, 9, 11, 13, 15, 17, 19);
		IntSummaryStatistics stats = primes.stream().mapToInt(x -> x).summaryStatistics();

		System.out.println("Highest prime number in List : " + stats.getMax());
		System.out.println("Lowest prime number in List : " + stats.getMin());
		System.out.println("Sum of all prime numbers : " + stats.getSum());
		System.out.println("Average of all prime numbers : " + stats.getAverage());

	}

	/**
	 * 通过复制不同的值创建一个子列表
	 * 
	 * 使用Stream的distinct()方法过滤集合中重复元素。
	 */
	public static void listDistinct() {
		List<Integer> numbers = Arrays.asList(9, 10, 3, 4, 7, 3, 4);
		List<Integer> distinct = numbers.stream().map(i -> i * i).distinct().collect(Collectors.toList());
		System.out.printf("Original List : %s,  Square Without duplicates : %s %n", numbers, distinct);
	}

	/**
	 * 对集合中每个元素应用函数
	 * 
	 * 我们经常需要对集合中元素运用一定的功能，如表中的每个元素乘以或除以一个值等等.
	 * 
	 * 将字符串转换为大写，然后使用逗号串起来。
	 */
	public static void listCollect() {
		List<String> G7 = Arrays.asList("USA", "Japan", "France", "Germany", "Italy", "U.K.", "Canada");
		String G7Countries = G7.stream().map(x -> x.toUpperCase()).collect(Collectors.joining(","));
		System.out.println(G7Countries);
	}


	public static void main(String[] args) {
		// map();
		// mapLambda();
		// list();
		// listLambda();
		//
		// listSummaryStatistics();

		// listDistinct();
		//
		// listCollect();
	}
}
