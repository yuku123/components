package com.zifang.util.zex.demo.jdk.lang.reflect.proxy.staticproxy;

public class Client {

    public static void main(String[] args) {
        RealSubject real = new RealSubject();
        ProxySubject proxy = new ProxySubject(real);

        proxy.doSomething();
        proxy.doOtherthing();
    }
}