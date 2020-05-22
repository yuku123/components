package com.zifang.util.zex.interview.demo2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Test {


    private static Object createObject(String str){

        IAImplement iaImplement = new IAImplement(str);
        Object o = Proxy.newProxyInstance(Test.class.getClassLoader(), IAImplement.class.getInterfaces(), new IAInvocationHandler(iaImplement,str));
        return o;
    }

    public static void main(String[] args) {
        IA ia = (IA)createObject(IA.class.getName()+"$getName=Abc");
        System.out.println(ia.getName());
        ia = (IA)createObject(IA.class.getName()+"$getName=Bcd");
        System.out.println(ia.getName());

        IB ib = (IB)createObject(IB.class.getName()+"$getIBName=Bcdss");
        System.out.println(ib.getIBName());
        ib = (IB)createObject(IB.class.getName()+"$getName=Bcd");
        System.out.println(ib.getIBName());

    }
}
