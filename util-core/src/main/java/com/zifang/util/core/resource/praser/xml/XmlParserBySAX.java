package com.zifang.util.core.resource.praser.xml;

import java.io.File;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlParserBySAX {

	public static void main(String[] args) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(new File("student.xml"), new MyHandler2());
	}

	public static class MyHandler2 extends DefaultHandler {
		private Stack<String> stack = new Stack<String>();
		private String name;
		private String gender;
		private String age;
		private String pre;

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			// stack.push(qName);
			pre = qName;
			for (int i = 0; i < attributes.getLength(); i++) {
				String attrName = attributes.getQName(i);
				String attrValue = attributes.getValue(i);
				System.out.println(attrName + " = " + attrValue);
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			// String tag = stack.peek();
			String tag = pre;
			if ("����".equals(tag)) {
				name = new String(ch, start, length);
			} else if ("�Ա�".equals(tag)) {
				gender = new String(ch, start, length);
			} else if ("����".equals(tag)) {
				age = new String(ch, start, length);
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			// stack.pop(); // ��ʾ��Ԫ���Ѿ�������ϣ���Ҫ��ջ�е���
			if ("ѧ��".equals(qName)) {
				System.out.println("������" + name);
				System.out.println("�Ա�" + gender);
				System.out.println("���䣺" + age);
				System.out.println();
			}
			pre = null;
		}

	}
}


