package com.zifang.util.core.anoation;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Inherited
@superInfo(superInfo = "superInfo")
public @interface ClassInfo {
    String className();
    String value() default "default-class-value";
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
@interface superInfo {
    String superInfo();
    String value() default "default-class-value";

}