package com.zifang.util.rpc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    private final ConcurrentHashMap<String, CompletableFuture<RpcResponse>> futureMap = new ConcurrentHashMap<>();

    public void setFuture(String requestId, CompletableFuture<RpcResponse> future) {
        futureMap.put(requestId, future);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
        CompletableFuture<RpcResponse> future = futureMap.remove(rpcResponse.getRequestId());
        if (future != null) {
            future.complete(rpcResponse);
        }
    }
}