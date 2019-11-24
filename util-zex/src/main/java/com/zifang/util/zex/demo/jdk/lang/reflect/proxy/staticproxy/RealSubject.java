package com.zifang.util.zex.demo.jdk.lang.reflect.proxy.staticproxy;

public class RealSubject implements Subject {
    @Override
    public void doSomething() {
        System.out.println("RealSubject doSomething");
    }
}