package com.zifang.util.zex.rmi.thrift;

public class HelloWorldImpl implements  HelloWorldService.Iface {
    public  HelloWorldImpl(){
    }
    @Override
    public  String sayHello(String username){
        return "hi " + username +"welcome to thrift world";
    }
}