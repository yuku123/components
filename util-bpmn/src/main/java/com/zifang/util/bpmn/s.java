package com.zifang.util.bpmn;


import com.zifang.util.bpmn.xml.TDefinitions;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;

public class s {
    public static void main(String[] args) throws JAXBException, XMLStreamException {

        InputStream stream = s.class.getClassLoader().getResourceAsStream("demo.bpmn");
        XMLInputFactory factory = XMLInputFactory.newFactory();
        XMLStreamReader reader = factory.createXMLStreamReader(stream);
        JAXBContext context = JAXBContext.newInstance(TDefinitions.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        TDefinitions definitions = unmarshaller.unmarshal(reader, TDefinitions.class).getValue();
        System.out.println("ccc");
    }
}
