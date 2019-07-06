package com.zifang.demo.jdk.java.util.concurent.packages;

public class RuntimeTest {

	public static void main(String[] args) throws InterruptedException {
		//获取运行的JVM虚拟机
		Runtime run = Runtime.getRuntime();
		//注册JVM虚拟机运行完后执行的事件
		run.addShutdownHook(new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("over!");
			}
		}));
		Thread.sleep(10000);
	}
}
