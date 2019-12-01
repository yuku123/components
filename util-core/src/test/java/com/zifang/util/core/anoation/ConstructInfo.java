package com.zifang.util.core.anoation;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR})
@Documented
@Inherited
public @interface ConstructInfo {
    String constructName();
}
