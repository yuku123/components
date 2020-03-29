package com.zifang.util.zex.demo.jdk.concurent.packages;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Example1 {

	public static void main(String[] args) {
		final BlockingQueue<String> queue = new ArrayBlockingQueue<String>(16);
		for(int i=0; i<4; i++){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true){
						try {
							String log = queue.take();
							parseLog(log);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					}
				}
			}).start();
		}
		for(int i=0; i<16; i++){
			final String log = "" + (i+1);
			try{
				queue.put(log);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
	public static void parseLog(String log){
		System.out.println(log+":"+(System.currentTimeMillis()));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
