package com.zifang.demo.jdk.java.lang.reflect.proxy.staticproxy;

public class RealSubject implements Subject {
    @Override
    public void doSomething() {
        System.out.println("RealSubject doSomething");
    }
}