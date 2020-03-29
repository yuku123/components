package com.zifang.util.zex.demo.jdk.concurent.packages;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ThreadScopeShareData {

	private static int data = 0;
	private static Map<Thread, Integer> threadData = new HashMap<Thread, Integer>();
	public static void main(String[] args) {
		for(int i=0; i<2; i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					data = new Random().nextInt();
					
					int d = data;
					threadData.put(Thread.currentThread(), d);
					System.out.println(Thread.currentThread().getName()
							+ " has put data of :" + data+"---d:"+d);
					new A().get();
					new B().get();
				}
			}).start();
		}
	}

	static class A {
		public int get() {
			
			System.out.println("A from "+Thread.currentThread().getName()
					+ " get put data of :" + data+"---d:"+threadData.get(Thread.currentThread()));
			return data;
		}
	}

	static class B {
		public int get() {
			System.out.println("B from "+Thread.currentThread().getName()
					+ " get put data of :" + data+"---d:"+threadData.get(Thread.currentThread()));
			return data;
		}
	}
}
