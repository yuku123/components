package com.zifang.util.core.demo.jdk.java.lang.reflect.reflect;

import java.lang.reflect.Constructor;

import org.junit.Test;

public class TestConstructor {

	@Test
	public void test1() throws Exception{
		String className = "cn.com.infcn.reflect.Person";
		Class clazz = Class.forName(className);
		Object obj = clazz.newInstance();
		Person p = (Person)obj;
		System.out.println(p);
	}
	
	@Test
	public void test2() throws ClassNotFoundException{
		String className = "cn.com.infcn.reflect.Person";
		Class clazz = Class.forName(className);
		
		Constructor[] cons = clazz.getDeclaredConstructors();
		for (Constructor c : cons) {
			System.out.println(c);
		}
		
	}
	
	@Test
	public void test3() throws Exception{
		String className = "cn.com.infcn.reflect.Person";
		Class clazz = Class.forName(className);
		
		Constructor cons = clazz.getDeclaredConstructor(String.class, int.class);
		cons.setAccessible(true);
		Person p = (Person)cons.newInstance("laoda",20);
		System.out.println(p);
	}
}
