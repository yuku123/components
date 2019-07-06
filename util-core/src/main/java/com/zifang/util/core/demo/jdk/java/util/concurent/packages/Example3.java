package com.zifang.util.core.demo.jdk.java.util.concurent.packages;

import java.util.concurrent.CopyOnWriteArrayList;

public class Example3 extends Thread{

	private TestDo2 testDo2;
	private String key;
	private String value;
	
	public Example3(String key, String key2,String value){
		this.testDo2 = TestDo2.getIntance();
		this.key = key;
		//这两个key不一样，因为 key+key2 都是变量，编译器不会优化
//		this.key = key+key2;
		/*
		a==b  常量相加，编译器编译时，知道 a==b ，代码编译时，就知道结果
		a = "1"+"";
		b = "1"+"";
		*/
		this.value = value;
	}
	
	public static void main(String[] args) {
		Example3 a = new Example3("1", "", "1");
		Example3 b = new Example3("1", "", "2");
		Example3 c = new Example3("3", "", "3");
		Example3 d = new Example3("4", "", "4");
		a.start();
		b.start();
		c.start();
		d.start();
	}
	
	public void run(){
		testDo2.doSame(key, value);
	}
}

class TestDo2{
	private TestDo2(){}
	private static TestDo2 _instance = new TestDo2();
	public static TestDo2 getIntance(){
		return _instance;
	}
	
	private CopyOnWriteArrayList<String> keys = new CopyOnWriteArrayList<String>();
	public void doSame(String key, String value){
		String o = key;
		if(!keys.contains(o)){
			keys.add(o);
		}else{
			for (String s : keys) {
				if(o.equals(s)){
					o = s;
				}
			}
		}
		synchronized (o) {
			try{
				Thread.sleep(1000);
				System.out.println(key+":"+value+":"+(System.currentTimeMillis()/1000));
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
}