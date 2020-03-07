package com.zifang.util.zex.demo.jdk.util.concurent.packages;

import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;

public class Example2 {

	public static void main(String[] args) {
		final SynchronousQueue<String> queue = new SynchronousQueue<String>();
		final Semaphore semaphore = new Semaphore(1);
		for(int i=0; i<10; i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					while(true){
						try {
							semaphore.acquire();
							String name = queue.take();
							String result = TestDo.doSame(name);
							System.out.println(result);
							semaphore.release();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
        }
		
		for(int i=0; i<10; i++){
			String input= i+"";
			try {
				queue.put(input);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
class TestDo{
	public static String doSame(String input){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String output = input + ":" + (System.currentTimeMillis() / 1000);
		return output;
	}
}
