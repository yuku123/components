package com.zifang.util.core.demo.jdk.lang.reflect.reflect;

@MyAnnotation(value="infcn")
public class Person extends Creature<String> implements Comparable,MyInterface {

	public String name;
	private int age;
	int id;
	
	//创建类时，尽量保留空参的构造器
	public Person(){
		super();
	}
	
	public Person(String name){
		super();
		this.name=name;
	}
	
	private Person(String name, int age){
		this.name=name;
		this.age=age;
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
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@MyAnnotation(value="abc123")
	public void show(){
		System.out.println("我是一个人！");
	}
	
	private Integer display(String nation, Integer i) throws Exception{
		System.out.println("我的国籍是："+nation);
		return i;
	}
	
	private void inner(){
		System.out.println("我的private方法：");
	}
	

	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + "]";
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}
	
	public static void info(){
		System.out.println("static method info");
	}

	class Brid{
		
	}
}
