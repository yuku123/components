package com.zifang.util.core.anoation;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class App {

    public static void main(String[] args) throws ClassNotFoundException {

        //Class clazz = AnnotationParsing.class.getClassLoader().loadClass(("com.zifang.jdk.base.annotations.baseMethod.AnnotationExample"))

        Class clazz = WholeBase.class;
        handleMethd(clazz);
        handleField(clazz);
        handleClass(clazz);
    }

    private static void handleClass(Class clazz) {
        for(Annotation annotation : clazz.getAnnotations()){
            System.out.println(annotation.annotationType());
        }

        if(clazz.isAnnotationPresent(ClassInfo.class)){
            clazz.getAnnotation(ClassInfo.class);
        }
    }

    private static void handleField(Class clazz) {
        for(Field field : clazz.getDeclaredFields()){
            //field.setAccessible(true);
            System.out.println(field);
            if(field.isAnnotationPresent(FieldInfo.class)){
                FieldInfo[] s = field.getAnnotationsByType(FieldInfo.class);
                for (int i = 0; i < s.length; i++) {
                    System.out.println("Annotation in field----" + s[i].comments() + "----" + s[i].password());
                }
            }
        }
    }

    private static void handleMethd(Class clazz) {
        for (Method method : clazz.getMethods()) {
            //判断是否有这个注解
            if (method.isAnnotationPresent(MethodInfo.class)) {
                //遍历这个方法上的所有的注解
                for (Annotation anno : method.getDeclaredAnnotations()) {
                    System.out.println("Annotation in Method----" + method + "----" + anno);
                }
                MethodInfo methodAnno = method.getAnnotation(MethodInfo.class);
                System.out.println(methodAnno.author());
                if (methodAnno.revision() == 1) {
                    System.out.println("Method with revision no 1 = " + method);
                }
            }
        }
    }
}
