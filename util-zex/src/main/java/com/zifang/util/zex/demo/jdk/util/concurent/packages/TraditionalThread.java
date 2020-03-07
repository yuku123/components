package com.zifang.util.zex.demo.jdk.util.concurent.packages;

public class TraditionalThread {

	public static void main(String[] args) {

		Thread thread = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("1:" + Thread.currentThread().getName());
					System.out.println("2:" + this.getName());
				}
			}
		};
		thread.start();

		Thread thread2 = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("3:" + Thread.currentThread().getName());
				}
			}
		});
		thread2.start();
		
		
		new Thread(new Runnable() {
			
			//此runnable不会运行，因为 thread的run方法被重写了，掉用不了runnable的方法
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("run:" + Thread.currentThread().getName());
				}
				
			}
		}){
			public void run() {
				while (true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("thread:" + Thread.currentThread().getName());
				}
			}

        }.start();
	}
	
}
