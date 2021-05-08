package com.zifang.util.ioc;

import com.google.inject.Inject;

public class Client {

    private Service service;

    public Client() {
    }

    @Inject                                    //依赖注入必须的标注
    public Client(Service service) {
        System.out.println("This is constructor injector...");
        this.service = service;
    }

//	第二种，set注入方式
//	@Inject
//	public void setClient(Service service){
//		System.out.println("This is set injector...");
//		this.service = service;
//	}

    public void sayHello() {
        service.sayHello();
    }

}