package com.zifang.util.core.beans.converter;

public class Test {

    public static void s(Object o){
        System.out.println(o.getClass());
    }

    public static void main(String[] args) {
        s((byte)1);
        s((short)1);
        s('c');
        s(1);
        s(1.0f);
        s(1.0d);
        s(true);
    }
}
