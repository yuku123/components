package com.zifang.util.core.demo.jdk.java.util.concurent.packages.atomic;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerDemo {

	private static AtomicInteger ai = new AtomicInteger();

	//打印出AtomicInteger的valueOffset偏移量 为12
	public static void showValueOffset(AtomicInteger atomicInteger) throws NoSuchFieldException, IllegalAccessException {
		Field field = ai.getClass().getDeclaredField("valueOffset");
		field.setAccessible(true);
		System.out.println("AtomicInteger类内的valueOffset偏移量为："+field.getLong(ai));
	}
	public static void test1() {

		ai.addAndGet(3);
		ai.incrementAndGet();
		System.out.println(ai.compareAndSet(4, 3));
		
		ai.getAndDecrement();
		ai.getAndIncrement();
	}
	
	public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
		test1();
		
		System.out.println(ai);
	}
}
