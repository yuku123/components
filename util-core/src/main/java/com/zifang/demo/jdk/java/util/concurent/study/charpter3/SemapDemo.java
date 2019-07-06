package com.zifang.demo.jdk.java.util.concurent.study.charpter3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemapDemo implements Runnable{
	
	public Semaphore semaphore = new Semaphore(5);//允许5个线程对这个参数进行同事的访问
	
	public SemapDemo() {}

	@Override
	public void run() {
		try {
			semaphore.acquire();
			Thread.sleep(2000);
			System.out.println(Thread.currentThread().getId()+":done");
			semaphore.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// TODO: handle finally clause
		}
	}
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newFixedThreadPool(5);
		SemapDemo semapDemo = new SemapDemo();
		for(int i = 0;i<20;i++) {
			exec.submit(semapDemo);
		}
		
	}

}
