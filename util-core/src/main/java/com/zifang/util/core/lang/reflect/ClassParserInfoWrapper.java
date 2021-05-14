package com.zifang.util.core.lang.reflect;

import lombok.Data;

import java.lang.reflect.Type;

@Data
public class ClassParserInfoWrapper {

    private Class<?> clazz;
    private Type type;
}