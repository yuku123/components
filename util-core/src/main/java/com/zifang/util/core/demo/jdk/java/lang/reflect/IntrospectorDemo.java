package com.zifang.util.core.demo.jdk.java.lang.reflect;

import java.beans.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 内省(IntroSpector)是Java语言对JavaBean 类属性、事件的一种处理方法。 例如类A中有属性name,
 * 那我们可以通过getName,setName 来得到其值或者设置新的值。 通过getName/setName 来访问name属性，这就是默认的规则。
 * Java中提供了一套API 用来访问某个属性的getter/setter方法，通过这些API
 * 可以使你不需要了解这个规则，这些API存放于包java.beans 中。
 * 
 * 一般的做法是通过类Introspector的getBeanInfo方法获取某个对象的BeanInfo
 * 信息,然后通过BeanInfo来获取属性的描述器(PropertyDescriptor),通过这个属性描述器就可以获取某个属性对应的getter/setter方法,然后我们就可以通过反射机制来调用这些方法。
 * 
 * 我们又通常把javabean的实例对象称之为值对象（Value Object），因为这些bean中通常只有一些信息字段和存储方法，没有功能性方法。
 * 一个JavaBean类可以不当JavaBean用，而当成普通类用。JavaBean实际就是一种规范，当一个类满足这个规范，这个类就能被其它特定的类调用。一个类被当作javaBean使用时，
 * JavaBean的属性是根据方法名推断出来的，它根本看不到java类内部的成员变量(即便没有属性也没关系，IntroSpector是假设性的操作)。
 * 去掉set前缀，然后取剩余部分，如果剩余部分的第二个字母是小写的，则把剩余部分的首字母改成小的。
 * 
 * 
 * 除了反射用到的类需要引入外，内省需要引入的类如下所示，它们都属于java.beans包中的类，自己写程序的时候也不能忘了引入相应的包或者类。
 * 下面代码片断是设置某个JavaBean类某个属性的关键代码：
 *
 */
public class IntrospectorDemo {
	public static void main(String[] args)
			throws IntrospectionException, InvocationTargetException, IllegalAccessException {
		User user = new User("zhangsan", 21);
		String propertyName = "name";
		PropertyDescriptor pd = new PropertyDescriptor(propertyName, user.getClass());// 直接指定要访问的属性
		Method readMethod = pd.getReadMethod();// 获取到读方法
		Object invoke = readMethod.invoke(user, null);// 反射机制调用
		System.out.println("名字：" + invoke);
		pd.getWriteMethod().invoke(user, "lisi");
		invoke = readMethod.invoke(user, null);
		System.out.println("名字：" + invoke);

		// 获取整个Bean的信息
		// BeanInfo beanInfo= Introspector.getBeanInfo(user.getClass());
		BeanInfo beanInfo = Introspector.getBeanInfo(user.getClass(), Object.class);// 在Object类时候停止检索，可以选择在任意一个父类停止

		System.out.println("所有属性描述：");
		PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();// 获取所有的属性描述
		for (PropertyDescriptor propertyDescriptor : pds) {
			System.out.println(propertyDescriptor.getName());
		}
		System.out.println("所有方法描述：");
		for (MethodDescriptor methodDescriptor : beanInfo.getMethodDescriptors()) {
			System.out.println(methodDescriptor.getName());
			// Method method = methodDescriptor.getMethod();
		}
	}

	static class User {
		private String name;
		private int age;

		public User(String name, int age) {
			this.name = name;
			this.age = age;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}
	}
}