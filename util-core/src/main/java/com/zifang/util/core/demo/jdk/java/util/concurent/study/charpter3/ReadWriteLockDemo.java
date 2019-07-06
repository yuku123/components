package com.zifang.util.core.demo.jdk.java.util.concurent.study.charpter3;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo{

	private static Lock lock = new ReentrantLock();
	private static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private static Lock readLock = readWriteLock.readLock();
	private static Lock writeLock = readWriteLock.writeLock();

	private int value;

	public Object handleRead(Lock lock) {
		try {
			lock.lock();
			Thread.sleep(1000);
			return value;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return null;
	}
	
	public void handleWrite(Lock lock,int index) {
		try {
			lock.lock();
			Thread.sleep(1000);
			value = index;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public static void main(String[] args) {
		ReadWriteLockDemo readWriteLockDemo = new ReadWriteLockDemo();
		Runnable readRunnable = new Runnable() {
			@Override
			public void run() {
				readWriteLockDemo.handleRead(readLock);
				//readWriteLockDemo.handleRead(lock);
			}
		};
		
		Runnable writeRunnable = new Runnable() {
			@Override
			public void run() {
				readWriteLockDemo.handleWrite(writeLock, new Random().nextInt());
				//readWriteLockDemo.handleWrite(lock, new Random().nextInt());
			}
		};
		for(int i = 0;i<18;i++) {
			new Thread(readRunnable).start();
		}
		for(int i =18;i<20;i++) {
			new Thread(writeRunnable).start();
		}
		
		
		
	}
	
}
