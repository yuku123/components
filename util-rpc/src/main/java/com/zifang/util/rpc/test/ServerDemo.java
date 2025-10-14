package com.zifang.util.rpc.test;

import com.zifang.util.rpc.RpcConstants;
import com.zifang.util.rpc.RpcServer;

public class ServerDemo {
    public static void main(String[] args) throws InterruptedException {
        RpcServer server = new RpcServer(RpcConstants.PORT);
        server.registerService(HelloService.class, new HelloServiceImpl());
        server.start();
    }
}