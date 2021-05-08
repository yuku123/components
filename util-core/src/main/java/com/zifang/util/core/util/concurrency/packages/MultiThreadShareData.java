package com.zifang.util.core.util.concurrency.packages;

public class MultiThreadShareData {

	public static void main(String[] args) {
		ShareData myData = new ShareData();
		for(int i=0; i<2; i++){
			new Thread(new MyRunnable1(myData)).start();
			new Thread(new MyRunnable2(myData)).start();
		}
	}
	
	
}

class MyRunnable1 implements Runnable{
	private ShareData myData;
	public MyRunnable1(ShareData myData){
		this.myData = myData;
	}
	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {
			myData.increment();
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
	}
}

class MyRunnable2 implements Runnable{
	private ShareData myData;
	public MyRunnable2(ShareData myData){
		this.myData = myData;
	}
	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {
			myData.decrement();
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
	}
}

class ShareData{
	private int i = 0;
	public synchronized void increment(){
		i++;
		System.out.println("i++"+i);
	}
	public synchronized void decrement(){
		i--;
		System.out.println("i--"+i);
	}
}
