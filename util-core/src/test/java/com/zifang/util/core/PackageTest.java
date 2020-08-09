package com.zifang.util.core;

import com.zifang.util.core.util.ClassUtil;

import java.util.Set;

public class PackageTest {
    public static void main(String[] args) {
        Set<Class<?>> set = ClassUtil.searchClasses("com.google");
        for (Class<?> aClass : set) {
            System.out.println(aClass);
        }
    }
}
