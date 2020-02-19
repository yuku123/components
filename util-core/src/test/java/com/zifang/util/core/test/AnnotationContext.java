package com.zifang.util.core.test;

import com.zifang.util.core.reflect.ClassScannerUtils;

import java.util.Set;

public class AnnotationContext {

    public AnnotationContext(String packageName) {
        Set<Class<?>> set =  ClassScannerUtils.searchClasses(packageName);
        for (Class<?> aClass : set) {
            System.out.println(aClass.getName());
        }
    }
}
