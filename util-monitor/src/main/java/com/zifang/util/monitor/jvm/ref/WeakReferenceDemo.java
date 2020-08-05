package com.zifang.util.monitor.jvm.ref;

//import cn.com.infcn.lang.ref.SoftReferenceDemo.Bean;


/**
 * 弱引用所指向的对象只要进行 GC，就会自动进行回收，get() 返回 null。
 * 
 *
 *
 */
public class WeakReferenceDemo {

//	public static void test1.groovy() {
//		WeakReference<Bean> bean = new WeakReference<Bean>(new Bean("name", 10));
//		System.gc();
//		System.runFinalization();
//
//		System.out.println(bean.get());// “null”
//	}
//
//	/**
//	 * JVM参数：-Xmx2m - Xms2m
//	 *
//	 * 总结：WeakReference 与 SoftReference 具有相同的特性，也会视内存使用情况来判断是否自动回收。取第 100 个对象时，返回为 null。
//	 */
//	public static void test2() {
//		Reference<Bean>[] referent = new WeakReference[100000];
//		for (int i = 0; i < referent.length; i++) {
//			referent[i] = new WeakReference<Bean>(new Bean("mybean:" + i, 100));
//		}
//
//		System.out.println(referent[100].get());// “null”
//	}

	public static void main(String[] args) {

	}
}
