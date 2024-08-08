package com.zifang.util.zex.source;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.stream.Collectors;

public class ClassPrinter {
    public static void main(String[] args) {
        Class c = AbstractQueuedSynchronizer.class;

        for(Method method : c.getDeclaredMethods()){
            String str = "";
            str = str + method.getReturnType().getName() +" " + method.getName();
            str = str + "(" + String.join(", ",Arrays.stream(method.getParameterTypes()).map(e->e.getName()).collect(Collectors.toList()))+")";
            System.out.println(str);
        }
    }
}
