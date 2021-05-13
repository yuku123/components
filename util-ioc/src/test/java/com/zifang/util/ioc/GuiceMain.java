package com.zifang.util.ioc;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class GuiceMain {

    public static void main(String[] args) {

        Service service = new ServiceImp();
//		Service service = new MockService();  // 第二个service的实现

        Client client = new Client(service);    //构造器注入

        Injector injector = Guice.createInjector(new TestModule());//向Guice要注射器
        injector.injectMembers(client);
        client.sayHello();
    }
}