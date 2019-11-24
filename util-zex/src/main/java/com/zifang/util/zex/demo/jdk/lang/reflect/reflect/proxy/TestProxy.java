package com.zifang.util.zex.demo.jdk.lang.reflect.reflect.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//动态代理使用, 反射是动态语言的体现
interface Subject{
	void action();
}

//被代理类
class RealSubject implements Subject{

	@Override
	public void action() {
		System.out.println("我是被代理类，记得要执行哦！么么~~");
	}
	
}

class MyInvocationHandler implements InvocationHandler{
	Object obj;  //实现了接口的被代理类对象的声明
	//1、给被代理类的对象实例化 2、返回一个代理类的对象
	public Object blind(Object obj){
		this.obj = obj;
		return Proxy.newProxyInstance(obj.getClass().getClassLoader(), 
				obj.getClass().getInterfaces(), this);
	}
	
	//当通过代理类的对象发起对被重写的方法调用时，都会转化为对如下的invoke方法的调用
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		begin();
		Object returnVal = method.invoke(obj, args);
		end();
		return returnVal;
	}
	
	public void begin(){
		System.out.println("===begin===");
	}
	public void end(){
		System.out.println("===end===");
	}
	
}


public class TestProxy {

	public static void main(String[] args) {
		//1、被代理类的对象
		RealSubject realSubject = new RealSubject();
		//2、创建一个实现了InvacationHandler接口的对象
		MyInvocationHandler handler = new MyInvocationHandler();
		//3、调用blind()方法，动态的返回一个同样实现了real所在类实现的接口Subject的代理类的对象
		//此时sub就是代理类的对象
		Subject sub = (Subject)handler.blind(realSubject);
		//转到对InvocationHandler接口的实现类的invoke()方法的调用
		sub.action();
		
		//在举一例
		ClothFactory nike = new NikeClothProduct();
		ClothFactory cf = (ClothFactory)handler.blind(nike);
		cf.productCloth();
	}
}
