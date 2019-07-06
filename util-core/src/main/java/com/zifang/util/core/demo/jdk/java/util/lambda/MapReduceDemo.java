package com.zifang.util.core.demo.jdk.java.util.lambda;

import java.util.Arrays;
import java.util.List;

public class MapReduceDemo {

	public static void map() {
		List<Integer> costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);

		costBeforeTax.stream().map((cost) -> cost + .12 * cost).forEach(System.out::println);
	}

	public static void reduce() {
		List<Integer> costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
		double bill = costBeforeTax.stream().map((cost) -> cost + cost * 0.12).reduce((cost, sum) -> sum + cost ).get();
		System.out.println(bill);
	}

	public static void main(String[] args) {
		map();
		reduce();
	}
}
