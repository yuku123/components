package com.zifang.util.praser.xml;

import java.io.FileOutputStream;
import java.io.FileWriter;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class XmlParserByDOM4J {

	public static void main(String[] args) throws Exception {

		Element root = DocumentHelper.createElement("student");
		Document document = DocumentHelper.createDocument(root);

		root.addAttribute("name", "zhangsan");

		Element helloElement = root.addElement("hello");
		Element worldElement = root.addElement("world");

		helloElement.setText("hello");
		worldElement.setText("world");

		helloElement.addAttribute("age", "20");

		XMLWriter xmlWriter = new XMLWriter();
		xmlWriter.write(document);

		OutputFormat format = new OutputFormat("    ", true);

		XMLWriter xmlWriter2 = new XMLWriter(new FileOutputStream("student2.xml"), format);
		xmlWriter2.write(document);

		XMLWriter xmlWriter3 = new XMLWriter(new FileWriter("student3.xml"), format);

		xmlWriter3.write(document);
		xmlWriter3.close();
	}

}
