package com.zifang.util.workflow.annoation;


import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EngineService {
    String name();
    String engine() default "spark";
}
