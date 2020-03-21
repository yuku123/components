package com.zifang.util.core.io;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlUtil {
    private static final Logger logger = Logger.getLogger(XmlUtil.class);

    /**
     * 获得根据节点信息
     * @param xml               xml字符串
     * @return
     */
    public static Element getRootElement(String xml){
        Document doc = null;
        Element root = null;

        try {
            doc = DocumentHelper.parseText(xml);
            root = doc.getRootElement();

        } catch (Exception ex) {
            // TODO: handle exception
            logger.error("解释xml文件出现异常:" + ex.getMessage());
        }
        return root;
    }

    /**
     * 获得指定元素下所有节点属性及值
     * @param element
     * @return
     */
    public static Map getNodeValues(Element element){
        Element root = null;
        Map map = new HashMap();
        try {
            List list = element.elements();
            Element e = null;
            for (int i = 0; i < list.size(); i++) {
                e = (Element)list.get(i);
                map.put(e.getName(), e.getText());
            }
        } catch (Exception ex) {
            // TODO: handle exception
            logger.error("获得指定元素下所有节点属性及值出现异常： " + ex.getMessage());
        }
        return map;
    }

    /**
     * 获得指定节点指定属性值
     * @param element                   元素名称
     * @param attributeName             属性名称
     * @return
     */
    public static String getElementAttributeValue(Element element,String attributeName){
        String value = "";
        try {
            value = element.attributeValue("attributeName");
        } catch (Exception ex) {
            // TODO: handle exception
            logger.error("获得指定节点指定属性值出现异常： " + ex.getMessage());
        }
        return value;
    }


    public static void main(String[] args) {
        try {
            String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Response><Result>0</Result><Mo num=\"2\"><Item id=\"1\" content=\"上行01\" from_mobile=\"13500000000\" to_port=\"0001\" rec_time=\"2013-07-20 13:23:45\"/><Item id=\"2\" content=\"上行02\" from_mobile=\"13600000000\" to_port=\"0002\" rec_time=\"2013-07-20 13:23:45\"/></Mo></Response >";
            System.out.println(xml);
            Map map = XmlUtil.getNodeValues(XmlUtil.getRootElement(xml));
            String result = (String)map.get("Result");
            System.out.println("result = " + result );

            Element mo = XmlUtil.getRootElement(xml).element("Mo");
            System.out.println("mo = " + mo);
            int num = Integer.parseInt(mo.attributeValue("num"));
            System.out.println("size = " + num);
            Element item = null;
            List list = mo.elements();

            for (int i = 0; i < list.size(); i++) {
                item = (Element)list.get(i);
                System.out.println("id = " + item.attributeValue("id") + " content = " + item.attributeValue("content") + " from_mobile = " + item.attributeValue("from_mobile") + " to_port = " + item.attributeValue("to_port") + " rec_time = " + item.attributeValue("rec_time"));
            }

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("ERROR:" + e.getMessage());
        }

    }

}
