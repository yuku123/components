package com.zifang.util.zex.bust.chapter9;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Map;

@AnnotationTest("类上的注解")
public class TestAnnotation<@AnnotationTest("类变量类型(泛型)上的注解") T0, T1> {

    @AnnotationTest("成员变量上的注解")
    private Map<@AnnotationTest("成员变量泛型上的注解") String, String> map;

    @AnnotationTest("成员方法上的注解")
    public void Test(@AnnotationTest("方法参数上的注解") String s, @AnnotationTest("方法参数上的注解2") String s2) {}

    @AnnotationTest("构造函数上的注解")
    public TestAnnotation() {}


    public static void main(String[] args) throws NoSuchMethodException {
        Class<TestAnnotation> clazz = TestAnnotation.class;
        //获取类上的注解
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation);
        }
        //是否存在注解
        if (clazz.isAnnotationPresent(AnnotationTest.class)) {
            AnnotationTest annotation1 = clazz.getAnnotation(AnnotationTest.class);
            System.out.println(annotation1.value());
        }


        //获取类的类型变量(泛型)的注解
        TypeVariable<Class<TestAnnotation>>[] typeParameters = clazz.getTypeParameters();
        for (TypeVariable typeVariable : typeParameters) {
            System.out.println(typeVariable.getName());
            Annotation[] annotations1 = typeVariable.getAnnotations();
            for (Annotation annotation : annotations1) {
                System.out.println(annotation);
            }
        }

        //获取成员变量上的注解
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getName());
            for (Annotation annotation : field.getAnnotations()) {
                System.out.println(annotation);
            }
        }

        //获取成员变量泛型的注解
        for (Field field : fields) {
            AnnotatedParameterizedType annotatedType = (AnnotatedParameterizedType) field.getAnnotatedType();
            AnnotatedType[] typeArguments = annotatedType.getAnnotatedActualTypeArguments();
            for (AnnotatedType typeArgument : typeArguments) {
                for (Annotation annotation : typeArgument.getAnnotations()) {
                    System.out.println(annotation);
                }
            }
        }

        //获取成员方法上的注解
        Method test = clazz.getMethod("Test", String.class, String.class);
        for (Annotation annotation : test.getAnnotations()) {
            System.out.println(annotation);
        }

        //获取成员方法上的参数注解
        for (Parameter parameter : test.getParameters()) {
            for (Annotation annotation : parameter.getAnnotations()) {
                System.out.println(annotation);
            }
        }

        //获取构造函数上的注解
        Constructor<TestAnnotation> constructor = clazz.getConstructor();
        for (Annotation annotation : constructor.getAnnotations()) {
            System.out.println(annotation);
        }

    }
}
