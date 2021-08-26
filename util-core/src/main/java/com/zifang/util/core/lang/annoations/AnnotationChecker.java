package com.zifang.util.core.lang.annoations;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.reflect.AnnotatedElement;

/**
 * 注解的验证类，包含多个判断的逻辑
 *
 * @author zifang
 */
public class AnnotationChecker {

    /**
     * 是否会保存到 Javadoc 文档中
     *
     * @param annotationType 注解类
     * @return 是否会保存到 Javadoc 文档中
     */
    public static boolean isDocumented(Class<? extends Annotation> annotationType) {
        return annotationType.isAnnotationPresent(Documented.class);
    }

    /**
     * 是否可以被继承，默认为 false
     *
     * @param annotationType 注解类
     * @return 是否会保存到 Javadoc 文档中
     */
    public static boolean isInherited(Class<? extends Annotation> annotationType) {
        return annotationType.isAnnotationPresent(Inherited.class);
    }

    /**
     * 检查是否实现了这个注解
     *
     * @param annotationClass  注解类型
     * @param annotatedElement 需要判断的元素
     */
    public static boolean hasAnnotationOn(Class<? extends Annotation> annotationClass, final AnnotatedElement annotatedElement) {
        return annotatedElement.isAnnotationPresent(annotationClass);
    }
}
