package com.zifang.util.praser.xml.stax;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * StreamFilter示例程序
 * 
 */
public class TestStreamFilter implements StreamFilter {

	public static void main(String[] args) {
		TestStreamFilter t = new TestStreamFilter();
		t.listUsers();
	}

	@Override
	public boolean accept(XMLStreamReader reader) {
		try {
			while (reader.hasNext()) {
				int event = reader.next();
				// 只接受元素的开始
				if (event == XMLStreamConstants.START_ELEMENT) {
					// 只保留user元素
					if ("user".equalsIgnoreCase(reader.getLocalName())) {
						return true;
					}
				}
				if (event == XMLStreamConstants.END_DOCUMENT) {
					return true;
				}
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return false;
	}

	public XMLStreamReader getFilteredReader() {
		String xmlFile = TestStreamFilter.class.getResource("/").getFile()
				+ "cn/com/infcn/stax/users.xml";
		XMLInputFactory factory = XMLInputFactory.newFactory();
		XMLStreamReader reader;
		try {
			reader = factory.createXMLStreamReader(new FileReader(xmlFile));
			// 创建带有过滤器的读取器实例
			XMLStreamReader freader = factory
					.createFilteredReader(reader, this);
			return freader;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void listUsers() {
		XMLStreamReader reader = getFilteredReader();
		try {
			// 列出所有用户的名称
			while (reader.hasNext()) {
				// 过滤工作已交由过滤器完成，这里不需要再做
				System.out.println("Name="
						+ reader.getAttributeValue(null, "name"));

				if (reader.getEventType() != XMLStreamConstants.END_DOCUMENT) {
					reader.next();
				}
			}
			reader.close();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

}
