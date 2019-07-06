package com.zifang.demo.jdk.java.lang.annotations;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
@Inherited
public @interface ParameterInfo {
    String setString();
}
