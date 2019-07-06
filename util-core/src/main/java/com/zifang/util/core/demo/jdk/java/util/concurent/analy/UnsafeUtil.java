package com.zifang.util.core.demo.jdk.java.util.concurent.analy;

import sun.misc.Unsafe;
import java.lang.reflect.Field;

public class UnsafeUtil {

    /**
     * 获得一个unsafe实例类
     *
     * unsafe实力类是不能被jdk外的代码获取的
     *
     * */
    public static Unsafe getUnsageInstance(){
        Field f = null;
        Unsafe unsafe = null;
        try {
            f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return unsafe;

    }







}
