package com.zifang.util.core.demo.jdk.util.concurent.study.charpter3;

import java.util.concurrent.locks.ReentrantLock;

public class ReenterantLock implements Runnable{
	
	public static ReentrantLock lock = new ReentrantLock();//重入锁
	public static int i = 0;
	
	@Override
	public void run() {
		for(int j =0;j<1000;j++) {
			lock.lock();
			try {
				i++;
			} finally {
				System.out.println(Thread.currentThread().getName()+i);
				lock.unlock();
			}
		}
		System.out.println(i);
	}
	public static void main(String[] args) throws InterruptedException {
		ReenterantLock reenterantLock = new ReenterantLock();
		Thread t1 = new Thread(reenterantLock);
		t1.setName("T1");
		Thread t2 = new Thread(reenterantLock);
		t2.setName("T2");
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		System.out.println(i);
	}
}
