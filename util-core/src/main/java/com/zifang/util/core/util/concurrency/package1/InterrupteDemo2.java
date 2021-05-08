package com.zifang.util.core.util.concurrency.package1;

import java.util.concurrent.TimeUnit;

/**
 * Thread 类还有其他可以检查线程是否被中断的方法。
 * 		例如，这个静态方法interrupted()能检查正在运行的线程是否被中断。
 * isInterrupted()和interrupted() 方法有着很重要的区别。
 * 		第一个不会改变interrupted属性值，但是第二个会设置成false。 
 * 		interrupted()方法是一个静态方法，建议使用isInterrupted()方法。 
 * 在之前提到的，线程是可以忽略中断指令的，但是并这不是我们所期望的行为。
 *
 */
public class InterrupteDemo2 {

	public static void main(String[] args) throws InterruptedException {
		MyThread t = new MyThread();
		t.start();
		TimeUnit.SECONDS.sleep(1);
		t.interrupt();
		System.out.println("end...");
	}

	static class MyThread extends Thread {
		int num = 0;
		boolean flag = true;

		@Override
		public void run() {
			while (flag) {
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					System.out.println("interrupted:"+e.getMessage());
					//相应中断操作
					flag = false;
				}
			}
		}
	}
}
