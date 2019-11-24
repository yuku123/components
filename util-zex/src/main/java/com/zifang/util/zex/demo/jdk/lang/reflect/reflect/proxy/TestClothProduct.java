package com.zifang.util.zex.demo.jdk.lang.reflect.reflect.proxy;

//静态代理的使用

interface ClothFactory{
	void productCloth();
}

//被代理类
class NikeClothProduct implements ClothFactory{

	@Override
	public void productCloth(){
		System.out.println("Nike工厂生产一批衣服");
	}
}
//代理类
class ProxyFactory implements ClothFactory{
	ClothFactory cf;
	
	//创建代理类对象时，实际传入一个被代理类的对象
	public ProxyFactory(ClothFactory cf){
		this.cf = cf;
	}
	@Override
	public void productCloth() {
		System.out.println("代理类开始执行，收代理费$1000");
		cf.productCloth();
	}
	
}

public class TestClothProduct{
	public static void main(String[] args) {
		//创建被代理类的对象
		ClothFactory cf = new NikeClothProduct();
		//创建代理类的对象
		ProxyFactory pf = new ProxyFactory(cf);
		pf.productCloth();
	}
}