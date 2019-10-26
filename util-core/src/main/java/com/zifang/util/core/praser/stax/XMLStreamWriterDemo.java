package com.zifang.util.core.praser.stax;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class XMLStreamWriterDemo {
	public static void main(String[] args) throws XMLStreamException, FactoryConfigurationError {
		
		XMLStreamWriter xsw = XMLOutputFactory.newInstance().createXMLStreamWriter(System.out);
		xsw.writeStartDocument("UTF-8","1.0");
		xsw.writeEndDocument();
		xsw.writeStartElement("person");
		xsw.writeStartElement("id");
		xsw.writeCharacters("1");
		xsw.writeEndElement();
		xsw.writeStartElement("name");
		xsw.writeCharacters("laoda");
		xsw.writeEndElement();
		xsw.writeEndElement();
		xsw.flush();
		xsw.close();
	}
	
	
}
