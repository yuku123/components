package com.zifang.util.core.util.concurrency.packages;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class PhaserDemo1 {

	public static void main(String[] args) {
		Phaser phaser = new Phaser(2);
		
		Thread t1 = new Thread(new MyThread(phaser, "one"));
		t1.start();
		Thread t2 = new Thread(new MyThread(phaser, "two"));
		t2.start();
		
	}
}

class MyThread implements Runnable{
	Phaser phaser = null;
	String info = null;
	public MyThread(Phaser phaser, String info) {
		this.phaser = phaser;
		this.info = info;
	}
	@Override
	public void run() {
		phaser.arriveAndAwaitAdvance();
		System.out.println(info+": start...");
		if(info.equals("one")){
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else{
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		phaser.arriveAndAwaitAdvance();
		System.out.println(info+": end..."+phaser.isTerminated());
		phaser.arriveAndDeregister();
		System.out.println("..."+phaser.isTerminated());
	}
	
}

