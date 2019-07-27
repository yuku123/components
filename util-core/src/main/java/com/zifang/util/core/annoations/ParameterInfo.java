package com.zifang.util.core.annoations;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
@Inherited
public @interface ParameterInfo {
    String setString();
}
