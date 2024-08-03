package com.zifang.util.zex.bust.chapter10;

import java.lang.reflect.Modifier;

public class ParserClassTest001 {

    public static void main(String[] args) {

        // 获得类基础信息
        Class<?> c = ParserClassTest001.class;

        System.out.println("类包名:"+c.getPackage().getName());
        System.out.println("类名:"+c.getName());
        System.out.println("类短名:"+c.getSimpleName());


        System.out.println("类是否是接口类型:"+c.isInterface());
        System.out.println("类是否是基本变量类型:"+c.isPrimitive());
        System.out.println("类是否是数组类型:"+c.isArray());



    }
}
