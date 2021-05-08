package com.zifang.util.core.io.parser.xml;

import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

@Slf4j
public class XmlBeanUtil {

    public static String beanToXml(Object obj, Class<?> load) {
        String xmlStr = null;
        try {
            JAXBContext context = JAXBContext.newInstance(load);
            Marshaller marshaller = context.createMarshaller();
            // 去掉生成xml的默认报文头
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            xmlStr = writer.toString();
        } catch (JAXBException e) {
            log.error("实体转为xml时出错!", e);
        }
        return xmlStr;
    }

    public static <T> T xmlToBean(String str, Class<T> load) {
        Object object = null;
        try {
            JAXBContext context = JAXBContext.newInstance(load);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            object = unmarshaller.unmarshal(new StringReader(str));
        } catch (JAXBException e) {
            log.error("xml转换到实体时出错!", e);
        }
        return (T) object;

    }
}
