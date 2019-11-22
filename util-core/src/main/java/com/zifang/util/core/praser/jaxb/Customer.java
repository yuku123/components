//package com.zifang.util.core.praser.jaxb;
//
//import java.util.Set;
//
//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlElementWrapper;
//import javax.xml.bind.annotation.XmlRootElement;
//import javax.xml.bind.annotation.XmlType;
//import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
//import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
//
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlRootElement(name = "customer")
//@XmlType(name = "customer", propOrder = { "id", "name", "age", "addr", "mobile", "book"})
//public class Customer {
//	@XmlAttribute(name = "id", required = true)
//	private int id;
//
//	@XmlElement(name = "name")
//	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
//	private String name;
//
//	@XmlElement(name = "addr")
//	private String addr;
//
//	@XmlElement(name = "mobile")
//	private String mobile;
//
//	@XmlElement(name = "age")
//	private int age;
//
//	@XmlElementWrapper(name = "books")
//	@XmlElement(name = "book")
//	private Set<Book> book;
//
//
//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public String getAddr() {
//		return addr;
//	}
//
//	public void setAddr(String addr) {
//		this.addr = addr;
//	}
//
//	public String getMobile() {
//		return mobile;
//	}
//
//	public void setMobile(String mobile) {
//		this.mobile = mobile;
//	}
//
//	public int getAge() {
//		return age;
//	}
//
//	public void setAge(int age) {
//		this.age = age;
//	}
//
//	public Set<Book> getBook() {
//		return book;
//	}
//
//	public void setBook(Set<Book> book) {
//		this.book = book;
//	}
//
//	@Override
//	public String toString() {
//		return "Customer [id=" + id + ", name=" + name + ", age=" + age + ", book=" + book + "]";
//	}
//
//}