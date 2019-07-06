package com.zifang.util.core.demo.jdk.java.lang.annotations;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
@Inherited
public @interface ParameterInfo {
    String setString();
}
