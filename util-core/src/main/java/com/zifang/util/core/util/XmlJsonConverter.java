//package com.zifang.util.core.util;
//
//import com.google.gson.*;
//import org.dom4j.*;
//import org.dom4j.dom.DOMText;
//import org.dom4j.io.OutputFormat;
//import org.dom4j.io.SAXReader;
//import org.dom4j.io.XMLWriter;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.StringWriter;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author zifang
// * <p>
// * 你要变得强大，强大到配得上你想要的东西
// */
//public class XmlJsonConverter {
//
//    /**
//     * 通用转换 xml -> json
//     *
//     * @param xml 等待装换的xml
//     * @return 已经转换了之后的xml
//     */
//    public static String convertXmlToJson(String xml) {
//        Document document = produceDocument(xml);
//        JsonObject jsonObject = new JsonObject();
//        Element element = document.getRootElement();
//        JsonObject p = new JsonObject();
//        solveElement(p, element);
//        jsonObject.add(element.getName(), p);
//        return jsonObject.toString();
//    }
//
//    private static void solveElement(JsonObject jsonObject, Element element) {
//        for (Attribute attribute : element.attributes()) {
//            jsonObject.addProperty("-" + attribute.getName(), attribute.getValue());
//        }
//
//        Map<String, List<Element>> nodeList = new LinkedHashMap<>();
//        for (Element item : element.elements()) {
//            List<Element> list = nodeList.computeIfAbsent(item.getName(), k -> new ArrayList<>());
//            list.add(item);
//        }
//
//        for (Map.Entry<String, List<Element>> entry : nodeList.entrySet()) {
//            List<Element> elements = entry.getValue();
//            if (elements.size() == 1) {
//                Element item = elements.get(0);
//                if (item.elements().size() == 0) {
//                    jsonObject.addProperty(item.getName(), item.getText());
//                }
//                JsonObject jsonObject1 = new JsonObject();
//                solveElement(jsonObject1, item);
//                jsonObject.add(item.getName(), jsonObject1);
//
//            } else if (elements.size() > 1) {
//                JsonArray jsonArray = new JsonArray();
//
//                for (Element element1 : elements) {
//                    JsonObject o = new JsonObject();
//                    solveElement(o, element1);
//                    jsonArray.add(o);
//                }
//                jsonObject.add(entry.getKey(), jsonArray);
//            }
//        }
//    }
//
//    private static Document produceDocument(String xml) {
//        InputStream inputStream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
//        SAXReader saxReader = new SAXReader();
//        Document doc = null;
//        try {
//            doc = saxReader.read(inputStream);
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
//        return doc;
//    }
//
//    /**
//     * 通用转换 json -> xml
//     *
//     * @param json 等待装换的json
//     * @return xml
//     */
//    public static String convertJsonToXml(String json) throws IOException {
//
//        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
//
//        Document doc = DocumentHelper.createDocument();
//
//        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
//            Element element = doc.addElement(entry.getKey());
//            solveJson(entry.getKey(), entry.getValue(), element);
//        }
//
//        StringWriter sw = new StringWriter();
//        OutputFormat format = OutputFormat.createPrettyPrint();
//        format.setEncoding("utf-8");
//        XMLWriter xmlWriter = new XMLWriter(sw, format);
//        xmlWriter.write(doc);
//        return sw.toString();
//    }
//
//    private static void solveJson(String elementKey, JsonElement jsonElement, Element doc) {
//        if (jsonElement instanceof JsonObject) {
//            for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
//                if (entry.getValue() instanceof JsonObject) {
//                    Element element = doc.addElement(entry.getKey());
//                    solveJson(entry.getKey(), entry.getValue(), element);
//                } else if (entry.getValue() instanceof JsonPrimitive) {
//                    if (entry.getKey().startsWith("-")) {
//                        doc.addAttribute(entry.getKey().replace("-", ""), entry.getValue().getAsString());
//                    } else {
//                        doc.addElement(entry.getKey()).add(new DOMText(entry.getValue().getAsString()));
//                    }
//                } else if (entry.getValue() instanceof JsonArray) {
//                    JsonArray jsonArray = (JsonArray) entry.getValue();
//                    for (JsonElement value : jsonArray) {
//                        Element element = doc.addElement(entry.getKey());
//                        solveJson(entry.getKey(), value, element);
//                    }
//                }
//            }
//        } else if (jsonElement instanceof JsonArray) {
//            JsonArray jsonArray = (JsonArray) jsonElement;
//            for (JsonElement value : jsonArray) {
//                Element element = doc.addElement(elementKey);
//                solveJson(elementKey, value, element);
//            }
//        }
//
//    }
//}
