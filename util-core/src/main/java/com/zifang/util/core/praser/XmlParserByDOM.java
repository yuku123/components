package com.zifang.util.core.praser;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class XmlParserByDOM {
	public static void main(String[] args) throws Exception {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new File("util-workflow/src/main/resources/workflow.xml"));
		Element root = doc.getDocumentElement();
		parseElement(root);
	}

	private static void parseElement(Element element) {
		String tagName = element.getNodeName();

		NodeList children = element.getChildNodes();

		System.out.print("<" + tagName);

		NamedNodeMap map = element.getAttributes();

		if (null != map) {
			for (int i = 0; i < map.getLength(); i++) {
				Attr attr = (Attr) map.item(i);

				String attrName = attr.getName();
				String attrValue = attr.getValue();

				System.out.print(" " + attrName + "=\"" + attrValue + "\"");
			}
		}

		System.out.print(">");

		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			short nodeType = node.getNodeType();

			if (nodeType == Node.ELEMENT_NODE) {
				parseElement((Element) node);
			} else if (nodeType == Node.TEXT_NODE) {
				System.out.print(node.getNodeValue());
			} else if (nodeType == Node.COMMENT_NODE) {
				System.out.print("<!--");

				Comment comment = (Comment) node;

				String data = comment.getData();

				System.out.print(data);

				System.out.print("-->");
			}
		}

		System.out.print("</" + tagName + ">");
	}
}
