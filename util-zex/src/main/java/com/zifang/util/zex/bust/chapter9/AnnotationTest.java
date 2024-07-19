package com.zifang.util.zex.bust.chapter9;

import java.lang.annotation.*;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Target({ElementType.TYPE,
        ElementType.METHOD,
        ElementType.FIELD,
        ElementType.TYPE_USE,
        ElementType.PARAMETER,
        ElementType.CONSTRUCTOR})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AnnotationTest {
    String value() default "";
}