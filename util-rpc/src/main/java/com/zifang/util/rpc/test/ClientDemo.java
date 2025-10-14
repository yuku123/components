package com.zifang.util.rpc.test;

import com.zifang.util.rpc.RpcClient;
import com.zifang.util.rpc.RpcConstants;
import com.zifang.util.rpc.RpcProxy;

public class ClientDemo {
    public static void main(String[] args) throws Exception {
        RpcClient client = new RpcClient(RpcConstants.DEFAULT_HOST, RpcConstants.PORT);
        try {
            RpcProxy proxy = new RpcProxy(client);
            HelloService helloService = proxy.getProxy(HelloService.class);

            String result = helloService.sayHello("World");
            System.out.println(result);  // 输出: Hello, World!
        } finally {
            client.close(); // 调用关闭方法释放资源
        }
    }
}