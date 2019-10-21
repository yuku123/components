package com.zifang.util.core.resource.praser.xml.stax;

import com.zifang.util.praser.xml.stax.TestStreamFilter;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

/**
 * 测试StreamReaderDelegate
 * 
 */
public class TestStreamDelegate {

	public static void main(String[] args) {
		TestStreamDelegate t = new TestStreamDelegate();
		t.listUsers();
	}

	public XMLStreamReader getDelegateReader() {
		String xmlFile = TestStreamFilter.class.getResource("/").getFile()
				+ "cn/com/infcn/stax/users.xml";
		XMLInputFactory factory = XMLInputFactory.newFactory();
		XMLStreamReader reader;
		try {
			reader = new StreamReaderDelegate(factory
					.createXMLStreamReader(new FileReader(xmlFile))) {
				// 重写（Override）next()方法，增加过滤逻辑
				@Override
				public int next() throws XMLStreamException {
					while (true) {
						int event = super.next();
						// 保留用户元素的开始
						if (event == XMLStreamConstants.START_ELEMENT
								&& "user".equalsIgnoreCase(getLocalName())) {
							return event;
						} else if (event == XMLStreamConstants.END_DOCUMENT) {
							return event;
						} else {
							continue;
						}
					}
				}
			};
			return reader;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void listUsers() {
		XMLStreamReader reader = this.getDelegateReader();
		try {
			while (reader.hasNext()) {
				reader.next();
				if (reader.getEventType() != XMLStreamConstants.END_DOCUMENT) {
					// 列出用户的名称和年龄
					System.out.println("Name="
							+ reader.getAttributeValue(null, "name") + ";age="
							+ reader.getAttributeValue(null, "age"));
				}
			}
			reader.close();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

}
