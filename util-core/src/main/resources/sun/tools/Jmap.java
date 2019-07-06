package com.zifang.demo.temp.sun.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * jmap 是一个内存映射工具，它提供了另外一种不需要引发 OutOfMemoryErrors 就可以获取堆 dump
 * 文件的方法。我们稍微修改一下上面的程序看一下效果。
 * 
 * @author jijs
 *
 */
public class Jmap {

	private static Map map = new HashMap<>();

	/**
	 * 注意，现在我们不要消耗大量的内存，只是比较早结束并在进程关闭钩子里等待不让 JVM 退出。这样就允许我们用 jmap 连接这个进程获取珍贵的内存
	 * dump。 因此你可以用 jmap 的两个功能来实现，获取堆统计信息和触发一个堆 dump。
	 * 
	 * jmap -heap 1354（这里 1354 是程序运行的进程号），就可以获取一个很好的内存使用统计信息
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					System.out.println("Enter something, so I'll release the process");
					System.in.read();
					System.out.println("We have accumulated " + map.size() + " entries");
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		});

		for (int i = 0; i < 10000; i++) {
			map.put(Integer.toBinaryString(i), i);
		}
	}
}
