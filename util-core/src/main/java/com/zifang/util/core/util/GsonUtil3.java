//package com.zifang.util.core.util;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import org.springframework.util.StringUtils;
//
//import java.lang.reflect.Type;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author zifang
// */
//public class GsonUtil {
//
//    public static ObjectMapper objectMapper = null;
//    private static Gson gson = new GsonBuilder()
//            .setPrettyPrinting()
//            .serializeNulls()   //在序列化的时候不忽略null值
//            .create();
//    private static Gson gsonNoEscape = new GsonBuilder().disableHtmlEscaping().create();
//
//    static {
//        objectMapper = new ObjectMapper();
//        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//    }
//    public static <T> String objectToJsonStr(T object) {
//        if (object == null) {
//            return "{}";
//        }
//        try {
//            return objectMapper.writeValueAsString(object);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
////        return gson.toJson(object);
//    }
//
//    public static <T> String otj(T object) {
//        return objectToJsonStr(object);
//    }
//
//
//    public static <T> String otjBeautify(T object) {
//        if (object == null) {
//            return "{}";
//        }
//        try {
//            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
//        return objectMapper.convertValue(map, clazz);
////        return jsonStrToObject(objectToJsonStr(map), clazz);
//    }
//
//    public static <T> T fromJson(String str, Class<T> t) {
//        try {
//            return objectMapper.readValue(str, t);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static <T> T jsonStrToObject(String jsonStr, Class<T> classOfT) {
//        if (jsonStr == null || "".equals(jsonStr)) {
//            return null;
//        }
//        return fromJson(jsonStr, classOfT);
//    }
//
//    public static <T> T jsonStrToObject(String jsonStr, Type type) {
//        return gson.fromJson(jsonStr, type);
//    }
//
//    public static <T> T jsto(String jsonStr, Class<T> classOfT) {
//        if (jsonStr == null || "".equals(jsonStr)) {
//            try {
//                return classOfT.newInstance();
//            } catch (InstantiationException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        } else {
//            return jsonStrToObject(jsonStr, classOfT);
//        }
//        return jsonStrToObject(jsonStr, classOfT);
//    }
//
//
//    public static <T> T safeJsto(String jsonStr, Class<T> classOfT) {
//        if (jsonStr == null || "".equals(jsonStr)) {
//            try {
//                return classOfT.newInstance();
//            } catch (InstantiationException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        } else {
//            try {
//                return objectMapper.readValue(jsonStr, classOfT);
//            } catch (JsonProcessingException e) {
//                return null;
//            }
//        }
//        return null;
//    }
//
//    public static <T> T changeToSubClass(Object o, Class<T> t) {
//        return jsonStrToObject(objectToJsonStr(o), t);
//    }
//
//    public static Map<String, Object> toMap(Object o) {
//        try {
//            return objectMapper.convertValue(o, Map.class);
//        }catch (Exception e){
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static Map<String, Object> toMap(String json) {
//        if("".equals(json)){
//           return null;
//        }
//        try {
//            return objectMapper.readValue(json,Map.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//
//        }
//    }
//
//    public static List<Map<String, Object>> toList(String json) {
//        return (List<Map<String, Object>>) jsonStrToObject(json, List.class);
//    }
//
//    public static boolean isSame(Map<String, Object> m1, Map<String, Object> m2) {
//        if (m1 == null && m2 == null) {
//            return true;
//        }
//        if ((m1 == null && m2 != null) || (m1 != null && m2 == null)) {
//            return false;
//        }
//        for (Map.Entry<String, Object> entry : m1.entrySet()) {
//            if (m2.containsKey(entry.getKey())) {
//                if (entry.getValue() instanceof List) {
//                    List<Object> lm1 = (List<Object>) entry.getValue();
//                    List<Object> lm2 = (List<Object>) m2.get(entry.getKey());
//                    if (lm2.size() != lm1.size()) {
//                        return false;
//                    }
//                    for (Object o : lm1) {
//                        boolean flag = false;
//                        for (Object i : lm2) {
//                            boolean b = isSame((Map<String, Object>) o, (Map<String, Object>) i);
//                            if (b) {
//                                flag = true;
//                            }
//                        }
//                        if (!flag) {
//                            return false;
//                        }
//                    }
//                } else if (entry.getValue() instanceof Map) {
//                    boolean f = isSame((Map<String, Object>) m1.get(entry.getKey()), (Map<String, Object>) m2.get(entry.getKey()));
//                    if (!f) {
//                        return false;
//                    }
//                } else {
//                    Object o1 = entry.getValue();
//                    Object o2 = m2.get(entry.getKey());
//                    if (o1 instanceof Number || o2 instanceof Number) {
//                        String o1_s = o1 instanceof Number ? String.valueOf(((Number) o1).longValue()) : String.valueOf(o1);
//                        String o2_s = o2 instanceof Number ? String.valueOf(((Number) o2).longValue()) : String.valueOf(o2);
//                        if (!o1_s.equals(o2_s)) {
//                            return false;
//                        }
//                    } else if (!String.valueOf(o1).equals(String.valueOf(o2))) {
//                        return false;
//                    }
//                }
//            } else {
//                return false;
//            }
//        }
//
//        //m1 字段null m2非null 上面判断未包含进去 这边加一层判断
//        for (Map.Entry<String, Object> entry : m2.entrySet()) {
//            //m1中不存在的字段并且不为空
//            if (!m1.containsKey(entry.getKey()) && !StringUtils.isEmpty(entry.getValue())) {
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    public static <T> boolean isJson(String str, Class<T> clazz) {
//        try {
//            T map = objectMapper.readValue(str, clazz);
//        } catch (JsonProcessingException e) {
//            return false;
//        }
//        return true;
//    }
//}
