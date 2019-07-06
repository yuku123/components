package com.zifang.demo.jdk.javax.xml.jaxb;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * 一、简介
 * 
 * 1、概念是什么：（Java Architecture for XML Binding) 是一个业界的标准，即是一项可以根据XML
 * Schema产生Java类的技术。该过程中，JAXB也提供了将XML实例文档反向生成Java对象树的方法，并能将Java对象树的内容重新写到XML实例文档
 * 。有多种实现。
 * 
 * 2、JAXB中有什么：包含“xjc”工具和一个“schemagen”工具。 “xjc”工具可以用来将XML模式或其他类型模式文件（Java
 * 1.6试验性地支持RELAX NG，DTD以及WSDL）转换为Java类。Java类使用javax.xml.bind.annotation包下的Java
 * 标注，例如@XmlRootElement和@XmlElement。XML列表序列表示为java.spi.List类型的属性，
 * 通过JAXBContext可以创建Marshallers（将Java对象转换成XML）和Unmarshallers（将XML解析为Java对象）。
 * 另外的“schemagen”工具，能够执行“xjc”的反向操作，通过一组标注的Java类创建一个XML模式。
 * 
 * 
 * 二、依赖
 * 
 * JDK5以下开发需要的jar包：activation.jar、jaxb-api.jar、 jaxb-impl.jar、 jsr173-api.jar;
 * 如果是基于JDK6以上版本已经集成JAXB2的JAR，在目录{JDK_HOME}/jre/lib/rt.jar中。
 */
public class MarshallerDemo {

	// Marshaller
	public static void Object2Xml() {
		Customer customer = new Customer();
		customer.setId(100);
		customer.setName("suo");
		customer.setAge(29);
		customer.setAddr("Beijing");
		customer.setMobile("010-62670085");

		Book book = new Book();
		book.setId("1");
		book.setName("哈里波特");
		book.setPrice(100);

		Book book2 = new Book();
		book2.setId("2");
		book2.setName("苹果");
		book2.setPrice(50);

		Set<Book> bookSet = new HashSet<Book>();
		bookSet.add(book);
		bookSet.add(book2);

		customer.setBook(bookSet);

		try {
			File file = new File("D:\\file.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(customer, file);
			jaxbMarshaller.marshal(customer, System.out);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	// Unmarshaller
	public static void Xml2Object() {
		try {
			File file = new File("D:\\file.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Customer customer = (Customer) jaxbUnmarshaller.unmarshal(file);
			System.out.println(customer);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Object2Xml();
		Xml2Object();
	}
}
