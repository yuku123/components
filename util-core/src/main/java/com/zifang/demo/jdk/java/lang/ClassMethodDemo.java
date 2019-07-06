package com.zifang.demo.jdk.java.lang;


public class ClassMethodDemo {

	/**
	 * Class.isAssignableFrom()是用来判断一个类Class1和另一个类Class2是否相同或是另一个类的子类或接口。
	 *
	 * 格式为： Class1.isAssignableFrom(Class2)
	 *
	 * 调用者和参数都是java.lang.Class类型。 而instanceof是用来判断一个对象实例是否是一个类或接口的或其子类子接口的实例。
	 *
	 * 格式是：o instanceof TypeName
	 *
	 * 第一个参数是对象实例名，第二个参数是具体的类名或接口名，例如 String,InputStream。
	 *
	 */
	public static void assignableTest(){
		System.out.println("String是Object的父类:" + String.class.isAssignableFrom(Object.class));
		System.out.println("Object是String的父类:" + Object.class.isAssignableFrom(String.class));
		System.out.println("Object和Object相同:" + Object.class.isAssignableFrom(Object.class));
		System.out.println("str是Object的实例:"+new String() instanceof Object);
	}

	/**
	 * 1. Class.getResourceAsStream(String path) ：
	 *
	 * path 不以’/'开头时默认是从此类所在的包下取资源，以’/'开头则是从ClassPath根下获取。
	 * 其只是通过path构造一个绝对路径，最终还是由ClassLoader获取 资 源 。
	 */
	public static void test1() {
		//1. 要加载的文件和.class文件在同一目录下，例如：com.x.y 下有类me.class ,同时有资源文件myfile.xml
		ClassMethodDemo.class.getResourceAsStream("myfile.xml");

		//2. 在GetResourceAsStreamDemo.class目录的子目录下，例如：com.x.y 下有类GetResourceAsStreamDemo.class ,同时在 com.x.y.file 目录下有资源文件myfile.xml
		ClassMethodDemo.class.getResourceAsStream("file/myfile.xml");

		//第三：不在me.class目录下，也不在子目录下，例如：com.x.y 下有类me.class ,同时在 com.x.file 目录下有资源文件myfile.xml
		ClassMethodDemo.class.getResourceAsStream("/com/x/file/myfile.xml");
	}

	/**
	 * 2. Class.getClassLoader.getResourceAsStream(String path) ：
	 *
	 * 默认则是从ClassPath根下获取，path不能以’/'开头，最终是由ClassLoader获取资源。
	 */
	public static void test2() {
		//最前面不能加/
		ClassMethodDemo.class.getClassLoader().getResource("com/x/file/myfile.xml");
	}

	/**
	 * 3. ServletContext. getResourceAsStream(String path)：
	 * 默认从WebAPP根目录下取资源，Tomcat下path是否以’/'开头无所谓，当然这和具体的容器实现有关。
	 */
	public static void test3() {

	}

	/**
	 * 4. Jsp下的application内置对象就是上面的ServletContext的一种实现。
	 */
	public static void test4(){

	}



	public static void main(String[] args) {
		assignableTest();
	}
}