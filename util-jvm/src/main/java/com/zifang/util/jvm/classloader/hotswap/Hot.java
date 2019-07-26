package com.zifang.util.jvm.classloader.hotswap;



public class Hot {
	public void hot() {
		String test = "2";
		System.out.println(" version 1 : " + this.getClass().getClassLoader());
	}
}