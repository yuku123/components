package com.zifang.util.concurrency.charpter3;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchDemo implements Runnable{
	
	public static CountDownLatch end= new CountDownLatch(10);
	public static CountDownLatchDemo demo= new CountDownLatchDemo();

	@Override
	public void run() {
		try {
			Thread.activeCount();
			Thread.sleep(new Random().nextInt(10)*1000);
			System.out.println("check complete");
			end.countDown();
		} catch (Exception e) {

		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec=Executors.newFixedThreadPool(10);
		for(int i = 0;i<10;i++) {
			exec.submit(demo);
		}
		
		end.await();
		System.out.println("Fire!");
		exec.shutdown();
		
	}

}
