package com.zifang.demo.jdk.java.lang.innerclass;

/**
 * 关于成员内部类的继承问题。一般来说，内部类是很少用来作为继承用的。但是当用来继承的话，要注意两点：
　*　1）成员内部类的引用方式必须为 Outter.Inner.
　*　2）构造器中必须有指向外部类对象的引用，并通过这个引用调用super()。这段代码摘自《Java编程思想》
 */

class WithInner{
	class Inner{
		
	}
}

public class InheritInner extends WithInner.Inner{

	// 没有 InheritInner() 是不能通过编译的，一定要加上形参
	public InheritInner(WithInner wi){
		wi.super();	//必须有这句调用
	}
	
	public static void main(String[] args) {
		WithInner wi = new WithInner();
		InheritInner obj = new InheritInner(wi);
	}
}
