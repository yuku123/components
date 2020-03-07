package com.zifang.util.zex.demo.jdk.util.concurent.packages;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TraditionalThreadSynchronized {

	public static void main(String[] args) {
		TraditionalThreadSynchronized t = new TraditionalThreadSynchronized();
		t.init();
	}
	
	private void init(){
		final Outputer outputer = new Outputer();
		new Thread(){
			public void run() {
				while(true){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					outputer.output("infcn.com.cn");
				}
			}
        }.start();
		
		new Thread(){
			public void run() {
				while(true){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					outputer.output2("laodahahaha");
				}
			}
        }.start();
	}
	
	static class Outputer{
		Lock lock = new ReentrantLock();
		private void output(String name){
			int len = name.length();
			try{
				lock.lock();
				for(int i=0; i<len; i++){
					System.out.print(name.charAt(i));
				}
			}finally{
				lock.unlock();
			}
			System.out.println();
		}
		
		private void output2(String name){
			int len = name.length();
			try{
				lock.lock();
				for(int i=0; i<len; i++){
					System.out.print(name.charAt(i));
				}
			}finally{
				lock.unlock();
			}
			System.out.println();
		}
		
	}
}
