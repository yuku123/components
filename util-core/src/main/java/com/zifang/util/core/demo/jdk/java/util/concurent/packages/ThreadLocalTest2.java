package com.zifang.util.core.demo.jdk.java.util.concurent.packages;

import java.util.Random;

public class ThreadLocalTest2 {

	static ThreadLocal<Integer> x = new ThreadLocal<Integer>();
	static ThreadLocal<MyThreadScoreData2> myThreadScoreData = new ThreadLocal<MyThreadScoreData2>();
	
	public static void main(String[] args) {
		for (int i = 0; i < 2; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					int data = new Random().nextInt();
					MyThreadScoreData2 myData = new MyThreadScoreData2();
					myData.setName("name"+data);
					myData.setAge(data);
					System.out.println(Thread.currentThread().getName()
							+ " has put data of :" + data);
					myThreadScoreData.set(myData);
					x.set(data);
					new A().get();
					new B().get();
				}
			}).start();
		}
	}

	static class A {
		public int get() {

			System.out.println("A from " + Thread.currentThread().getName()
					+ " get put data of :" + x.get());
			MyThreadScoreData2 myData = myThreadScoreData.get();
			System.out.println("A from " + Thread.currentThread().getName()
					+ " getMyData :" + myData.getName()+","
					+myData.getAge());
			return x.get();
		}
	}

	static class B {
		public int get() {
			System.out.println("B from " + Thread.currentThread().getName()
					+ " get put data of :" + x.get());
			MyThreadScoreData2 myData = myThreadScoreData.get();
			System.out.println("B from " + Thread.currentThread().getName()
					+ " getMyData :" + myData.getName()+","
					+myData.getAge());
			return x.get();
		}
	}
}

class MyThreadScoreData2 {
	private String name;
	private int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
