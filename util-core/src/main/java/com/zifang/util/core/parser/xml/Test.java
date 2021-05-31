package com.zifang.util.core.parser.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Test {

    /**
     * XML转换为javabean
     * Jul 25, 2013 9:39:05 PM xuejiangtao添加此方法
     * @param <T>
     * @param xml
     * @param t
     * @return
     * @throws JAXBException
     */
    public static <T> T xmlToBean(String xml, T t) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(t.getClass());
        Unmarshaller um = context.createUnmarshaller();
        StringReader sr = new StringReader(xml);
        t = (T) um.unmarshal(sr);
        return t;
    }

    /**
     * javabean转换为XML
     * Jul 25, 2013 9:39:09 PM xuejiangtao添加此方法
     * @return
     * @throws JAXBException
     * @throws FileNotFoundException
     */
    public static <T> StringWriter beanToXml(T t) throws JAXBException, FileNotFoundException {
        JAXBContext context = JAXBContext.newInstance(t.getClass());
        Marshaller m = context.createMarshaller();
        StringWriter sw = new StringWriter();
        m.marshal(t, sw);
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);//是否格式化
        m.marshal(t, new FileOutputStream("test.xml"));
        return sw;
    }

    /**
     * Jul 24, 2013 10:12:39 PM xuejiangtao添加此方法
     * @param args
     * @throws JAXBException
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws JAXBException, FileNotFoundException {
        ReqHeader reqHeader = new ReqHeader();
        reqHeader.setReqNo("111");
        reqHeader.setAuthCode("DDDD");
        reqHeader.setSysId("jjj");

        SmsBody smsBody = new SmsBody();
        smsBody.setContent("类容讷讷");
        smsBody.setDestAddr("1589594");
        smsBody.setSourceAddr("9999");

        SmsBody smsBody1 = new SmsBody();
        smsBody1.setContent("asdf");
        smsBody1.setDestAddr("asdf");
        smsBody1.setSourceAddr("4fdf");


        SmsDeliverReq smsDeliverReq = new SmsDeliverReq();
        smsDeliverReq.setReqHeader(reqHeader);
        List<SmsBody> smsBodys = new ArrayList<SmsBody>();
        smsBodys.add(smsBody);
        smsBodys.add(smsBody1);
        smsDeliverReq.setSmsBodys(smsBodys);

        StringWriter sw = beanToXml(smsDeliverReq);
        System.out.println(sw.toString());

        SmsDeliverReq xmlToBean = xmlToBean(sw.toString(), smsDeliverReq);
        System.out.println(xmlToBean.toString());
    }
}
