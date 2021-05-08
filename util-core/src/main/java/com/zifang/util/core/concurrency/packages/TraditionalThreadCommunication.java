package com.zifang.util.core.concurrency.packages;

public class TraditionalThreadCommunication {

	public static void main(String[] args) {
		final Business business = new Business();
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i=0; i<5; i++){
					business.sub(i);
				}
			}
		}).start();
		
		for(int i=0; i<5; i++){
			business.main(i);
		}
	}

}

//锁不是放在线程里面，而是放在线程索要访问资源中的
class Business{
	boolean shouldSub=true;
	public synchronized void sub(int i){
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
	
	public synchronized void main(int i){
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