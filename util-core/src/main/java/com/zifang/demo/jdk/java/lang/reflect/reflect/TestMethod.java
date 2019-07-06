package com.zifang.demo.jdk.java.lang.reflect.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Test;

public class TestMethod {

	//��ȡ����ʱ��ķ���
	@Test
	public void test1(){
		Class clazz = Person.class;
		//1��getMethods():��ȡ����ʱ�༰�丸������������Ϊpublic�ķ���
		Method[] m1 = clazz.getMethods();
		for (Method m : m1) {
			System.out.println(m);
		}
		
		System.out.println();
		//1��getDeclaredMethods():��ȡ�����౾�����������еķ���
		Method[] m2 = clazz.getDeclaredMethods();
		for (Method m : m2) {
			System.out.println(m);
		}
	}
	
	//ע�⡢Ȩ�����η�������ֵ���͡����������β��б��쳣
	@Test
	public void test2(){
		Class clazz = Person.class;
		Method[] m1 = clazz.getDeclaredMethods();
		for (Method m : m1) {
			//1.ע��
			Annotation[] anns = m.getAnnotations();
			for (Annotation a : anns) {
				System.out.println("annotation:"+a);
			}
			//2.Ȩ�����η�
			String str = Modifier.toString(m.getModifiers());
			System.out.print(str+" ");
			//3.����ֵ����
			Class returnType = m.getReturnType();
			System.out.print(returnType.getName()+" ");
			//4.������
			System.out.print(m.getName());
			//5.�β��б�
			Class[] params = m.getParameterTypes();
			System.out.print("(");
			for (int i=0; i<params.length; i++) {
				System.out.print(params[i].getName()+" args- "+i);
			}
			System.out.print(")");
			//6.�쳣
			Class[] ex = m.getExceptionTypes();
			for (Class e : ex) {
				System.out.print(e.getName()+" ");
			}
			
//			System.out.println(m);
			System.out.println();
		}
	}
	
	//��������ʱָ���ķ���
	@Test
	public void test3() throws Exception{
		Class clazz = Person.class;
		//getMethod(String methodName, Class ... prams):��ȡ����ʱ��������Ϊpublic�ķ���
		Method m1 = clazz.getMethod("show");
		Person p = (Person)clazz.newInstance();
		//����ָ���ķ�����invoke(Oject obj, Object ... obj)
		Object returnVal = m1.invoke(p);
		System.out.println(returnVal);
		
		Method m2 = clazz.getMethod("toString");
		Object returnVal1 = m2.invoke(p);
		System.out.println(returnVal1);
		
		Method m3 = clazz.getMethod("info");
		m3.invoke(clazz);
		m3.invoke(null);
//		Method setAge = clazz.getMethod("setAge", age.getType());
//		setAge.invoke(p, 20);
//		System.out.println(p);
		
		//getDeclaredMethod(String methodName, class ... params):��ȡ����ʱ���еķ���
		Method m4 = clazz.getDeclaredMethod("display", String.class, Integer.class);
		m4.setAccessible(true);
		Object obj4 = m4.invoke(p, "laoda", 12);
		System.out.println(obj4.toString());
	}
}
