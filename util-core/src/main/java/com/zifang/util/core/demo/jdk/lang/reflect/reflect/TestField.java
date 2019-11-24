package com.zifang.util.core.demo.jdk.lang.reflect.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Test;

public class TestField {

	//��ȡ��Ӧ������ʱ�������
	@Test
	public void test1(){
		Class clazz = Person.class;
		//1��getFields()��ֻ�ܻ�ȡ������ʱ���м��丸������Ϊpublic������
		Field[] fields = clazz.getFields();
		for (Field field : fields) {
			System.out.println(field);
		}
		//2��getDeclaredFields():��ȡ����ʱ�����������е�����
		Field[] fields1 = clazz.getDeclaredFields();
		for (Field field : fields1) {
			System.out.println(field.getName());
		}
	}
	
	//Ȩ�����η� �������� ������
	//��ȡ���Եĸ�粿�ֵ�����
	@Test
	public void test2(){
		Class clazz = Person.class;
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			//1����ȡÿ�����Ե�Ȩ�����η�
			int i=f.getModifiers();
			System.out.print(Modifier.toString(i)+"\t");
			//2����ȡ���Եı�������
			Class type = f.getType();
			System.out.print(type+"\t");
			//3����ȡ������
			System.out.println(f.getName());
		}
	}
	
	//��������ʱ����ָ��������
	@Test
	public void test3() throws Exception{
		Class clazz = Person.class;
		//1����ȡָ��������
		//getField(String fieldName):��ȡ����ʱ��������Ϊpublic��ָ��������ΪfieldNamed������
		Field name = clazz.getField("name");
		//2����������ʱ��Ķ���
		Person p = (Person)clazz.newInstance();
		//3��������ʱ��ָ�������Ը�ֵ
		name.set(p, "Jerry");
		System.out.println(p);
		
		System.out.println();
		
		//getDeclaredField(String fieldName):��ȡ����ʱ����ָ������ΪfieldName������
		Field age = clazz.getDeclaredField("age");
		//��������Ȩ�����η������ƣ�Ϊ�˱�֤�����Ը�ֵ����Ҫ�ڲ���ǰʹ�ô����Կɱ�����
		age.setAccessible(true);
		age.set(p, 10);
		System.out.println(p);
		
//		Field id = clazz.getField("id");
		
	}
}
