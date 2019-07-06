package com.zifang.util.core.demo.jdk.javax.xml.stax;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * 列出所有用户
 *
 */
public class ListUsers {
	// 获得解析器
	public static XMLStreamReader getStreamReader() {
		String xmlFile = ListUsers.class.getResource("/").getFile() + "cn/com/infcn/stax/users.xml";
		XMLInputFactory factory = XMLInputFactory.newFactory();
		try {
			XMLStreamReader reader = factory.createXMLStreamReader(new FileReader(xmlFile));
			return reader;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 打印用户全部属性
	public static void listAllAttrs() {
		XMLStreamReader reader = ListUsers.getStreamReader();
		try {
			int count = 1;
			while (reader.hasNext()) {
				int event = reader.next();
				// 如果是元素的开始
				if (event == XMLStreamConstants.START_ELEMENT) {
					if ("user".equalsIgnoreCase(reader.getLocalName())) {
						System.out.print(count + ".");
						// 打印全部属性信息
						for (int index = 0; index < reader.getAttributeCount(); index++) {
							System.out.print(reader.getAttributeLocalName(index) + "="
									+ reader.getAttributeValue(index) + ";");
						}
						System.out.println();
						count++;
					}
				}
			}
			reader.close();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	// 列出所有信息
	@SuppressWarnings("unchecked")
	public static void listAllByXMLEventReader() {
		String xmlFile = ListUsers.class.getResource("/").getFile() + "cn/com/infcn/stax/users.xml";
		XMLInputFactory factory = XMLInputFactory.newInstance();
		try {
			// 创建基于迭代器的事件读取器对象
			XMLEventReader reader = factory.createXMLEventReader(new FileReader(xmlFile));
			// 遍历XML文档
			while (reader.hasNext()) {
				XMLEvent event = reader.nextEvent();
				// 如果事件对象是元素的开始
				if (event.isStartElement()) {
					// 转换成开始元素事件对象
					StartElement start = event.asStartElement();
					// 打印元素标签的本地名称
					System.out.print(start.getName().getLocalPart());
					// 取得所有属性
					Iterator attrs = start.getAttributes();
					while (attrs.hasNext()) {
						// 打印所有属性信息
						Attribute attr = (Attribute) attrs.next();
						System.out.print(":" + attr.getName().getLocalPart() + "=" + attr.getValue());
					}
					System.out.println();
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	// 列出所有用户名称
	public static void listNames() {
		XMLStreamReader reader = ListUsers.getStreamReader();
		// 遍历XML文档
		try {
			while (reader.hasNext()) {
				int event = reader.next();
				// 如果是元素的开始
				if (event == XMLStreamConstants.START_ELEMENT) {
					// 列出所有用户名称
					if ("user".equalsIgnoreCase(reader.getLocalName())) {
						System.out.println("Name:" + reader.getAttributeValue(null, "name"));
					}
				}
			}
			reader.close();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	// 列出所有用户的名称和年龄
	public static void listNamesAndAges() {
		XMLStreamReader reader = ListUsers.getStreamReader();
		try {
			while (reader.hasNext()) {
				// 跳过所有空白、注释或处理指令，到下一个START_ELEMENT
				int event = reader.nextTag();
				if (event == XMLStreamConstants.START_ELEMENT) {
					if ("user".equalsIgnoreCase(reader.getLocalName())) {
						System.out.println("Name:" + reader.getAttributeValue(null, "name") + ";Age:"
								+ reader.getAttributeValue(null, "age"));
					}
				}
			}
			reader.close();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ListUsers.listNames();
		// ListUsers.listNamesAndAges();
		ListUsers.listAllAttrs();
		ListUsers.listAllByXMLEventReader();
	}
}
