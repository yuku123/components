package com.zifang.util.core.demo.jdk.jvm.classloader.hotswap;


import com.zifang.util.core.demo.jdk.jvm.classloader.Test;

public class Hot {
	public void hot() {
		String test = "2";
		System.out.println(" version 1 : " + this.getClass().getClassLoader());
		Test.main(null);
	}
}