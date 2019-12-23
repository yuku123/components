package com.zifang.util.workflow.anoation;


import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EngineService {
    String name();
}
