package com.zifang.demo.jdk.java.util.concurent.packages;

import java.util.Random;

public class ThreadLocalTest {

	public static void main(String[] args) {
		for (int i = 0; i < 2; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					int data = new Random().nextInt();
					MyThreadScoreData.getThreadInstance().setName("name" + data);
					MyThreadScoreData.getThreadInstance().setAge(data);
					
					System.out.println(Thread.currentThread().getName()
							+ " has put data of :" + data);
					new A().get();
					new B().get();
					MyThreadScoreData.getThreadInstance().clearThreadLocal();
				}
			}).start();
		}
	}

	static class A {
		public void get() {
			MyThreadScoreData myData = MyThreadScoreData.getThreadInstance();
			System.out.println("A from " + Thread.currentThread().getName()
							+ " getMyData :" + myData.getName() + ","
							+ myData.getAge());
		}
	}

	static class B {
		public void get() {
			MyThreadScoreData myData = MyThreadScoreData.getThreadInstance();
			System.out.println("B from " + Thread.currentThread().getName()
							+ " getMyData :" + myData.getName() + ","
							+ myData.getAge());
		}
	}
}

class MyThreadScoreData {

	//把ThreadLocal封装起来，让调用的用户感受不到ThreadLocal的存在
	private static ThreadLocal<MyThreadScoreData> map = new ThreadLocal<MyThreadScoreData>();

	private MyThreadScoreData() {
	}

	public static MyThreadScoreData getThreadInstance() {
		MyThreadScoreData instance = map.get();
		if (instance == null) {
			instance = new MyThreadScoreData();
			map.set(instance);
		}
		return instance;
	}
	
	public void clearThreadLocal(){
		map.remove();
	}

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
