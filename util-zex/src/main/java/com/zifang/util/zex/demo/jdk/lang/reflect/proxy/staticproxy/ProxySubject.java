package com.zifang.util.zex.demo.jdk.lang.reflect.proxy.staticproxy;

public class ProxySubject implements Subject {
    private Subject real;

    public ProxySubject(Subject real) {
        this.real = real;
    }

    @Override
    public void doSomething() {
        System.out.println("ProxySubject before real doSomething");
        real.doSomething();
        System.out.println("ProxySubject end real doSomething");
    }

    public void doOtherthing() {
        System.out.println("ProxySubject doOtherthing");
    }
}
