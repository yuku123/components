package com.zifang.demo.jdk.java.lang.annotations;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Inherited
public @interface ClassInfo {
    String className();
}
