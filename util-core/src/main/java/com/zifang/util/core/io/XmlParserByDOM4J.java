package com.zifang.util.core.io;//package com.zifang.util.core.praser;
//
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//
//
//
//public class XmlParserByDOM4J {
//
//	public static void main(String[] args) throws Exception {
//
//		Element root = DocumentHelper.createElement("student");
//		Document document = DocumentHelper.createDocument(root);
//
//		root.addAttribute("name", "zhangsan");
//
//		Element helloElement = root.addElement("hello");
//		Element worldElement = root.addElement("world");
//
//		helloElement.setText("hello");
//		worldElement.setText("world");
//
//		helloElement.addAttribute("age", "20");
//
//		XMLWriter xmlWriter = new XMLWriter();
//		xmlWriter.write(document);
//
//		OutputFormat format = new OutputFormat("    ", true);
//
//		XMLWriter xmlWriter2 = new XMLWriter(new FileOutputStream("student2.xml"), format);
//		xmlWriter2.write(document);
//
//		XMLWriter xmlWriter3 = new XMLWriter(new FileWriter("student3.xml"), format);
//
//		xmlWriter3.write(document);
//		xmlWriter3.close();
//	}
//
//}
