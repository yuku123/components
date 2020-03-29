package com.zifang.util.io;//package com.zifang.util.core.praser;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.util.List;
//
//import org.jdom2.Attribute;
//import org.jdom2.Document;
//import org.jdom2.Element;
//import org.jdom2.input.SAXBuilder;
//import org.jdom2.output.Format;
//import org.jdom2.output.XMLOutputter;
//
//public class XmlParserByJDOM {
//	public static void main(String[] args) throws Exception {
//		SAXBuilder builder = new SAXBuilder();
//
//		Document doc = builder.build(new File("student.xml"));
//
//		Element element = doc.getRootElement();
//
//		System.out.println(element.getName());
//		// 获取第一�?
//		Element hello = element.getChild("学生");
//
//		System.out.println(hello.getText());
//
//		List<Element> els = element.getChildren();
//		for (Element el : els) {
//			List elList = el.getAttributes();
//			for (int i = 0; i < elList.size(); i++) {
//				Attribute attr = (Attribute) elList.get(i);
//
//				String attrName = attr.getName();
//				String attrValue = attr.getValue();
//
//				System.out.println(attrName + "=" + attrValue);
//			}
//			System.out.println("姓名:" + el.getChildText("姓名"));
//			System.out.println("性别:" + el.getChildText("性别"));
//			System.out.println("年龄" + el.getChildText("年龄"));
//			System.out.println("===============");
//		}
//
//		// 可以对XML进行编辑
//		Element addEl = new Element("学生");
//		Attribute attr = new Attribute("学号", "4");
//
//		Element addEl01 = new Element("姓名");
//		addEl01.setText("我爱�?");
//		addEl.addContent(addEl01);
//		Element addEl02 = new Element("性别");
//		addEl02.setText("�?");
//		addEl.addContent(addEl02);
//
//		addEl.setAttribute(attr);
//		element.addContent(addEl);
//
//		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
//
//		out.output(doc, new FileOutputStream("student2.xml"));
//
//	}
//
//}
