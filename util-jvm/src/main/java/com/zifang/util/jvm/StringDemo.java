package com.zifang.util.jvm;

public class StringDemo {

	public void test1() {
		String a = new String("ab");
		String b = new String("ab");
		String c = "ab";
		String d = "a" + "b";
		String e = "b";
		String f = "a" + e;

		System.out.println(b.intern() == a); // false
		System.out.println(b.intern() == c); // true
		System.out.println(b.intern() == d); // true
		System.out.println(b.intern() == f); // false
		System.out.println(b.intern() == a.intern());// true

		/**
		 * 由运行结果可以看出来，b.intern() == a和b.intern() == c可知，采用new
		 * 创建的字符串对象不进入字符串池，并且通过b.intern() == d和b.intern() ==
		 * f可知，字符串相加的时候，都是静态字符串的结果会添加到字符串池，如果其中含有变量（如f中的e）则不会进入字符串池中。
		 */
	}

	/**
	 * -XX:+PrintFlagsFinal 打印虚拟机的Global flags参数
	 * 在Java6以及Java 7 直到 Java7u40前，-XX:StringTableSize 参数默认值是1009。在Java7u40中它增长为60013（在Java8中也是同样的值）。
	 * 如果你不确定字符串常量池的使用情况，尝试使用 -XX:+PrintStringTableStatics 虚拟机参数。它将会在你程序结束时打印出你的字符串常量池的使用情况。
	 */
	public static void main(String[] args) {
		StringDemo d = new StringDemo();
//		d.test1.groovy();
		
//		String s = new String ("aaaa");
		String s = new StringBuilder().append("aa").append("aa").toString();
		System.out.println(s.intern()==s);
		
	}

}
