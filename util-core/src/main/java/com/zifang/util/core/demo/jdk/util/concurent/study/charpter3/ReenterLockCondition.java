package com.zifang.util.core.demo.jdk.util.concurent.study.charpter3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReenterLockCondition implements Runnable {

	public static ReentrantLock lock = new ReentrantLock();
	public static Condition condition = lock.newCondition();

	@Override
	public void run() {
		try {
			System.out.println("1:"+lock.isLocked());
			lock.lock();
			System.out.println("2:"+lock.isLocked());
			condition.await();
			System.out.println("3:"+lock.isLocked());
			System.out.println("Thread is going on");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		ReenterLockCondition reenterLockCondition = new ReenterLockCondition();
		Thread thread1 = new Thread(reenterLockCondition);
		thread1.start();
		Thread.sleep(1000);
		System.out.println("lock");
		lock.lock();//搞不懂这里为什么要加上lock呢？
		condition.signal();
		lock.unlock();

	}
}
