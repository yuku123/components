package com.zifang.util.core.lang.reflect;

import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TestOthers {

	//1、获取运行时类的父类
	@Test
	public void test1(){
		Class clazz = Person.class;
		Class superClass = clazz.getSuperclass();
		System.out.println(superClass);
	}
	
	//2、获取带泛型的父类
	@Test
	public void test2(){
		Class clazz = Person.class;
		Type type = clazz.getGenericSuperclass();
		System.out.println(type);
		System.out.println(type);

	}
	
	//3、获取父类的泛型
	@Test
	public void test3(){
		Class clazz = Person.class;
		ParameterizedType param = (ParameterizedType)clazz.getGenericSuperclass();
		Type[] type = param.getActualTypeArguments();
		for (Type t : type) {
			System.out.println(((Class)t).getName());
		}
	}
}
