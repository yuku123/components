package com.zifang.util.core.lang.dynamic;


import com.zifang.util.core.util.GsonUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DynamicClassUtil {

    public static DynamicClass parser(Class<?> clazz) {
        DynamicClass dynamicClass = new DynamicClass();
        dynamicClass.setAnnotations(parserAnnotations(Arrays.asList(clazz.getAnnotations())));
        dynamicClass.setInterface(clazz.isInterface());
        dynamicClass.setPackageName(clazz.getPackage().getName());
        dynamicClass.setClassName(clazz.getName());
        dynamicClass.setImplementClasses(parserClass(Arrays.asList(clazz.getInterfaces())));
        dynamicClass.setFields(parserFields(Arrays.asList(clazz.getDeclaredFields())));
        dynamicClass.setMethods(parserMethods(Arrays.asList(clazz.getDeclaredMethods())));
        return dynamicClass;
    }

    private static DynamicAnnotation parser(Annotation annotation) {
        return null;
    }

    public static DynamicField parser(Field field) {
        return null;
    }

    public static DynamicMethod parser(Method method) {
        return null;
    }

    public static List<DynamicClass> parserClass(List<Class<?>> clazzes) {
        return clazzes.stream().map(DynamicClassUtil::parser).collect(Collectors.toList());
    }

    public static List<DynamicMethod> parserMethods(List<Method> methods) {
        return methods.stream().map(DynamicClassUtil::parser).collect(Collectors.toList());
    }

    public static List<DynamicField> parserFields(List<Field> fields) {
        return fields.stream().map(DynamicClassUtil::parser).collect(Collectors.toList());
    }

    private static List<DynamicAnnotation> parserAnnotations(List<Annotation> annotations) {
        return annotations.stream().map(DynamicClassUtil::parser).collect(Collectors.toList());
    }

    public static Class<?> parser(DynamicClass dynamicClass) {
        return null;
    }

    public static String parserAsCode(DynamicClass dynamicBean) {
        return null;
    }

    public static String generateClassSource(DynamicClass dynamicBean) {
        return null;
    }

    public static List<String> generateClassSource(String json){
        return generateClassSource(GsonUtil.toMap(json));
    }
    public static List<String> generateClassSource(Map<String,Object> map){
        List<DynamicClass> dynamicClasses = parserAsDynamicClass(map);
        return dynamicClasses.stream().map(DynamicClass::generateSourceCode).collect(Collectors.toList());
    }

    private static List<DynamicClass> parserAsDynamicClass(Map<String, Object> map) {
        return parserAsDynamicClassUnit(map).collect();
    }
    private static DynamicClassUnit parserAsDynamicClassUnit(Map<String, Object> map) {
        DynamicClassUnit dynamicClassUnit = new DynamicClassUnit();
        DynamicClass dynamicClass = new DynamicClass();
        dynamicClass.setClassName("Bean_"+System.currentTimeMillis());
        for(Map.Entry<String,Object> entry : map.entrySet()){
            // 无值 归纳为String
            if(entry.getValue() == null){
                dynamicClass.addField(entry.getKey(),String.class, true, true);
                continue;
            }

            if(Map.class.isAssignableFrom(entry.getValue().getClass())){
                DynamicClassUnit sub = parserAsDynamicClassUnit((Map<String,Object>) entry.getValue());
                dynamicClass.addField(entry.getKey(),sub.getMain().getType(), true, true);
                dynamicClassUnit.getSub().add(sub);
            }

            if(List.class.isAssignableFrom(entry.getValue().getClass())){
                List<Object> list = (List<Object>) entry.getValue();
                Object o = list.get(0);
                if(o instanceof Map){
                    // result.addAll(parserAsDynamicClass((Map<String, Object>) o));
                } else if(o instanceof List){
                    // result.addAll(parserAsDynamicClass((Map<String, Object>) o));
                }
            }

            dynamicClass.addField(entry.getKey(),String.class, true, true);
        }

        dynamicClassUnit.setMain(dynamicClass);

        return dynamicClassUnit;
    }

}
