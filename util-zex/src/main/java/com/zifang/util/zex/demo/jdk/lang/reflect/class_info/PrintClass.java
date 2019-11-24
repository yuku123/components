package com.zifang.util.zex.demo.jdk.lang.reflect.class_info;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class PrintClass {

	public static void printConstructors(Class c1) {

		Constructor[] constructors = c1.getDeclaredConstructors();

		for (Constructor c : constructors) {
			String name = c.getName();
			System.out.print(" ");
			String modifiers = Modifier.toString(c.getModifiers());
			if (modifiers.length() > 0)
				System.out.print(modifiers + " ");
			System.out.print(name + "(");

			Class[] paramTypes = c.getParameterTypes();
			for (int i = 0; i < paramTypes.length; i++) {
				if (i > 0)
					System.out.print(", ");
				System.out.print(paramTypes[i].getName());
			}
			System.out.println(");");
		}
	}

	public static void printMethods(Class c1) {
		Method[] methods = c1.getDeclaredMethods();
		for (Method m : methods) {
			Class retType = m.getReturnType();
			String name = m.getName();

			System.out.print("  ");
			String modifiers = Modifier.toString(m.getModifiers());
			if (modifiers.length() > 0)
				System.out.print(modifiers + " ");
			System.out.print(retType.getName() + " " + name + "(");
			Class[] paraTypes = m.getParameterTypes();
			for (int i = 0; i < paraTypes.length; i++) {
				if (i > 0)
					System.out.print(", ");
				System.out.print(paraTypes[i].getName());
			}
			System.out.println(");");
		}
	}

	public static void printFields(Class c1) {
		Field[] fields = c1.getDeclaredFields();
		for (Field f : fields) {
			Class type = f.getType();
			String name = f.getName();
			System.out.print("  ");
			String modifiers = Modifier.toString(f.getModifiers());
			if (modifiers.length() > 0)
				System.out.println(type.getName() + " " + name + ";");
		}
	}
	
	public static void printInterface(Class c1) {
		Class<?> interfaces[] = c1.getInterfaces();//���Dog��ʵ�ֵ����нӿ�
        for (Class<?> inte : interfaces) {//��ӡ
            System.out.println(inte);
        }
	}

	public static void main(String[] args) throws ClassNotFoundException {
		String name = "java.spi.ArrayList";

		Class c1 = Class.forName(name);
		Class superc1 = c1.getSuperclass();
		String modifiers = Modifier.toString(c1.getModifiers());
		if (modifiers.length() > 0)
			System.out.print(modifiers + " ");
		System.out.print("class " + name);
		if (superc1 != null && superc1 != Object.class)
			System.out.print("extends " + superc1.getName());
		System.out.print("\n{\n");
		printConstructors(c1);
		System.out.println();
		printMethods(c1);
		System.out.println();
		printFields(c1);
		System.out.println("}");
		
		printInterface(superc1);
	}

}
