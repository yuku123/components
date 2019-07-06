package com.zifang.demo.jdk.java.lang.annotations;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR})
@Documented
@Inherited
public @interface ConstructInfo {
    String constructName();
}
