package com.zifang.util.core.praser.stax;

import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

public class XMLStreamReaderDemo {

	public static void test1() throws XMLStreamException{
		XMLInputFactory factory = XMLInputFactory.newFactory();
		//从当前所在路径下加载xml文件
//		InputStream is = XMLStreamReaderDemo.class.getResourceAsStream("stax_book.xml");
		//指定路径下加载xml文件
		InputStream is = XMLStreamReaderDemo.class.getClassLoader().getResourceAsStream("cn/com/infcn/stax/stax_book.xml");
		XMLStreamReader reader = factory.createXMLStreamReader(is);
		
		while(reader.hasNext()){
			int type = reader.next();
			
			if(type==XMLStreamConstants.START_ELEMENT){
				System.out.println(reader.getName());
			}else if(type==XMLStreamConstants.CHARACTERS){
				System.out.println(reader.getText().trim());
			}else if(type==XMLStreamConstants.END_ELEMENT){
				System.out.println("/"+reader.getName());
			}
		}
	}
	
	/**
	 * 读取属性
	 * @throws XMLStreamException
	 */
	public static void test2() throws XMLStreamException{
		XMLInputFactory factory = XMLInputFactory.newFactory();
		//从当前所在路径下加载xml文件
//		InputStream is = XMLStreamReaderDemo.class.getResourceAsStream("stax_book.xml");
		//指定路径下加载xml文件
		InputStream is = XMLStreamReaderDemo.class.getClassLoader().getResourceAsStream("cn/com/infcn/stax/stax_book.xml");
		XMLStreamReader reader = factory.createXMLStreamReader(is);
		
		while(reader.hasNext()){
			int type = reader.next();
			
			if(type==XMLStreamConstants.START_ELEMENT){
				QName qname = reader.getName();
				if("book".equals(qname.getLocalPart())){
					int count = reader.getAttributeCount();
					System.out.println("============");
					for (int i=0; i<count ; i++) {
						System.out.println(reader.getAttributeName(i)+":"+reader.getAttributeValue(i));
					}
				}
			}
		}
	}
	
	/**
	 * 读取指定值
	 * @throws XMLStreamException
	 */
	public static void test3() throws XMLStreamException{
		XMLInputFactory factory = XMLInputFactory.newFactory();
		//从当前所在路径下加载xml文件
//		InputStream is = XMLStreamReaderDemo.class.getResourceAsStream("stax_book.xml");
		//指定路径下加载xml文件
		InputStream is = XMLStreamReaderDemo.class.getClassLoader().getResourceAsStream("cn/com/infcn/stax/stax_book.xml");
		XMLStreamReader reader = factory.createXMLStreamReader(is);
		
		while(reader.hasNext()){
			int type = reader.next();
			
			if(type==XMLStreamConstants.START_ELEMENT){
				QName qname = reader.getName();
				if("title".equals(qname.getLocalPart())){
					System.out.print(reader.getElementText()+":");
				}else if("price".equals(qname.getLocalPart())){
					System.out.println(reader.getElementText());
				}
			}
		}
	}
	
	/**
	 * 迭代模式
	 * @throws XMLStreamException
	 */
	public static void test4() throws XMLStreamException{
		XMLInputFactory factory = XMLInputFactory.newFactory();
		//从当前所在路径下加载xml文件
//		InputStream is = XMLStreamReaderDemo.class.getResourceAsStream("stax_book.xml");
		//指定路径下加载xml文件
		InputStream is = XMLStreamReaderDemo.class.getClassLoader().getResourceAsStream("cn/com/infcn/stax/stax_book.xml");
		XMLEventReader reader = factory.createXMLEventReader(is);
		
		while(reader.hasNext()){
			XMLEvent event = reader.nextEvent();
			
			if(event.isStartElement()){
				QName qname = event.asStartElement().getName();
				if("title".equals(qname.getLocalPart())){
					System.out.print(reader.getElementText()+":");
				}else if("price".equals(qname.getLocalPart())){
					System.out.println(reader.getElementText());
				}
			}
		}
	}
	
	/**
	 * 迭代模式  过滤流
	 * @throws XMLStreamException
	 */
	public static void test5() throws XMLStreamException{
		XMLInputFactory factory = XMLInputFactory.newFactory();
		//从当前所在路径下加载xml文件
//		InputStream is = XMLStreamReaderDemo.class.getResourceAsStream("stax_book.xml");
		//指定路径下加载xml文件
		InputStream is = XMLStreamReaderDemo.class.getClassLoader().getResourceAsStream("cn/com/infcn/stax/stax_book.xml");
		XMLEventReader reader = factory.createFilteredReader(factory.createXMLEventReader(is), 
			new EventFilter() {
		
				@Override
				public boolean accept(XMLEvent event) {
					if(event.isStartElement()){
						return true;
					}
					return false;
				}
		});
		
		int num=0;
		while(reader.hasNext()){
			XMLEvent event = reader.nextEvent();
			num++;
			if(event.isStartElement()){
				QName qname = event.asStartElement().getName();
				if("title".equals(qname.getLocalPart())){
					System.out.print(reader.getElementText()+":");
				}else if("price".equals(qname.getLocalPart())){
					System.out.println(reader.getElementText());
				}
			}
		}
		System.out.println("循环次数："+num);
	}
	
	public static void main(String[] args) throws Exception{
		//test1.groovy、test2、test3 基于光标的模式查找
//		test1.groovy();
//		test2();
//		test3();
		
		//test4基于迭代的模式
//		test4();
		
		//test5过滤流
		test5();
	}
}
