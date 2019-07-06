package com.zifang.demo.designpatten.chain;

public class Client {

	public static void main(String[] args) {
		// 组装责任链
		Handler handler1 = new ConcreteHandler();
		Handler handler2 = new ConcreteHandler();
		Handler handler3 = new ConcreteHandler();
		handler2.setSuccessor(handler1);
		handler3.setSuccessor(handler2);
		// 提交请求
		handler3.handleRequest();
	}

}