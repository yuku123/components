package com.zifang.util.zex.demo.jdk.concurent.packages;

/**
 * Lock比传统线程模型中的synchronized方式更加面向对象，与生活中的锁类似，锁本身也应该是一个对象。
 * 两个线程执行的代码片段要实现同步互斥的效果，他们必须用同一个Lock对象。锁是上在代表要操作的
 * 资源的类的内部方法中，而不是线程代码中。
 */
public class LockTest {

	public static void main(String[] args) {
		LockTest t = new LockTest();
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
					Outputer.output3("laodahahaha");
				}
			}
        }.start();
	}
	
	static class Outputer{
		private void output(String name){
			int len = name.length();
			synchronized(Outputer.class){
				for(int i=0; i<len; i++){
					System.out.print(name.charAt(i));
				}
				System.out.println();
			}
		}
		
		private synchronized void output2(String name){
			int len = name.length();
			for(int i=0; i<len; i++){
				System.out.print(name.charAt(i));
			}
			System.out.println();
		}
		
		private synchronized static void output3(String name){
			int len = name.length();
				for(int i=0; i<len; i++){
					System.out.print(name.charAt(i));
				}
				System.out.println();
		}
	}
}
