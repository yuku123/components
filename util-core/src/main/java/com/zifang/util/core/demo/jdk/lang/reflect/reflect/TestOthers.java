package com.zifang.util.core.demo.jdk.lang.reflect.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Test;

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
	
	//4、获取实现的接口
	@Test
	public void test4(){
		Class clazz = Person.class;
		Class[] interfaces = clazz.getInterfaces();
		for (Class i : interfaces) {
			System.out.println(i);
		}
	}
	
	//5、获取所在的包
	@Test
	public void test5(){
		Class clazz = Person.class;
		Package pack = clazz.getPackage();
		System.out.println(pack);
	}
	
	//6、获取注解
	@Test
	public void test6(){
		Class clazz = Person.class;
		//runtime的类型才能获取到
		Annotation[] anns = clazz.getAnnotations();
		for (Annotation a : anns) {
			System.out.println(a);
		}
	}
}
