package com.zifang.util.zex.demo.jdk.util.concurent.packages;

public class TraditionalThreadCommunication2 {
	static boolean shouldSub = true;

	public static void main(String[] args) {
		TraditionalThreadCommunication2 t = new TraditionalThreadCommunication2();
		t.test();
		t.test2();
	}

	public void test() {
		new Thread() {
			public void run() {
				for(int i=0; i<5; i++){
					while(!shouldSub){
						try {
							wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					for(int j=1; j<=10; j++){
						System.out.println("sub: j="+j+"\t i="+i);
					}
					shouldSub = false;
					notify();
				}
			};
		}.start();
	}

	public void test2() {
		new Thread() {
			public void run() {
				for(int i=0; i<5; i++){
					while(shouldSub){
						try {
							wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					for(int j=1; j<=2; j++){
						System.out.println("main: j="+j+"\t i="+i);
					}
					shouldSub = true;
					notify();
				}
			}
		};
	}
}
