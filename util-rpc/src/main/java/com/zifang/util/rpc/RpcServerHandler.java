package com.zifang.util.rpc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.Map;

public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    // 存储服务实现类
    private final Map<String, Object> serviceMap;

    public RpcServerHandler(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }


    // 处理请求，反射调用服务方法
    private Object handle(RpcRequest request) throws Exception {
        String interfaceName = request.getInterfaceName();
        Object service = serviceMap.get(interfaceName);

        if (service == null) {
            throw new RuntimeException("Service not found: " + interfaceName);
        }

        Class<?> serviceClass = service.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(service, parameters);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        RpcResponse response = new RpcResponse();
        response.setRequestId(rpcRequest.getRequestId());

        try {
            Object result = handle(rpcRequest);
            response.setResult(result);
        } catch (Exception e) {
            response.setException(e);
        }

        channelHandlerContext.writeAndFlush(response);
    }
}