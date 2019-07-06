package com.zifang.demo.jdk.java.lang.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
@Inherited
public @interface FieldInfo {
    String name();
    String password();
    String comments() default "this is default parts";
}
