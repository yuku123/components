package com.zifang.util.core.demo.jdk.java.lang.innerclass;


/**
 * 一般来说，有4中内部类：常规内部类、静态内部类、局部内部类、匿名内部类。
 * 	 一.常规内部类
 * 	常规内部类没有用static修饰且定义在在外部类类体中。
 *	  1.常规内部类中的方法可以直接使用外部类的实例变量和实例方法。
 *	  2.在常规内部类中可以直接用内部类创建对象
 *
 *
 */
public class MyOuter1 {
	private int x = 100;

	// 创建内部类
	class MyInner1 {
		private String y = "Hello!";

		public void innerMethod() {
			System.out.println("内部类中 String = " + y);
			System.out.println("外部类中的x = " + x);// 直接访问外部类中的实例变量x
			outerMethod();
			System.out.println("x is " + MyOuter1.this.x);
		}
	}

	public void outerMethod() {
		x++;
	}

	public void makeInner() {
		// 在外部类方法中创建内部类实例
		MyInner1 in = new MyInner1();
		in.innerMethod();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MyOuter1 mo = new MyOuter1();
		// 使用外部类构造方法创建mo对象
		MyInner1 inner = mo.new MyInner1();// 常规内部类需要通过外部类的实例才能创建对象，与实例变量需要通过对象来访问相似
		// 创建inner对象
		inner.innerMethod();
		// TODO Auto-generated method stub
		mo.makeInner();
	}
}