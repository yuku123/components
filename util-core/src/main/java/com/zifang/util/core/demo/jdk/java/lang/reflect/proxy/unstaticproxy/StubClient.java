package com.zifang.util.core.demo.jdk.java.lang.reflect.proxy.unstaticproxy;

import com.zifang.util.core.demo.jdk.java.lang.reflect.proxy.staticproxy.RealSubject;
import com.zifang.util.core.demo.jdk.java.lang.reflect.proxy.staticproxy.Subject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class StubClient {
    public static void main(String[] args) throws Exception {
        StubClient client = new StubClient();
        //(2)通过为 Proxy 类指定 ClassLoader 对象和一组 interface 来创建动态代理类
        Class<?> proxyClass = Proxy.getProxyClass(Subject.class.getClassLoader(), new Class<?>[]{Subject.class});
        //(3)通过反射机制获得动态代理类的构造函数，其唯一参数类型是调用处理器接口类型
        Constructor<?> proxyClassConstructor = proxyClass.getConstructor(new Class<?>[]{InvocationHandler.class});
        //(4)通过构造函数创建动态代理类实例，构造时调用处理器对象作为参数被传入
        Object o = proxyClassConstructor.newInstance(client.handler);

        ////简化上述(2)(3)(4)步骤
        Object o2 = Proxy.newProxyInstance(Subject.class.getClassLoader(), new Class<?>[] {Subject.class}, client.handler);

        //(5)通过动态代理对象调用
        Subject subject = (Subject) o;
        //(6)输出委托类的结果
        subject.doSomething();
    }

    //(1)通过实现 InvocationHandler 接口创建自己的调用处理器
    private InvocationHandler handler = new InvocationHandler() {
        //创建委托类对象实例
        private RealSubject real = new RealSubject();

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //调用委托类的方法
            System.out.println("在invoker内部");
            return method.invoke(real, args);
        }
    };
}