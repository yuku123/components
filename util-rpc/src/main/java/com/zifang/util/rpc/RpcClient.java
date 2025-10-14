package com.zifang.util.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.CompletableFuture;

public class RpcClient {
    private final String host;
    private final int port;
    private Channel channel;
    private RpcClientHandler clientHandler;
    private EventLoopGroup group;

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
        init();
    }

    // 新增关闭方法
    public void close() {
        try {
            channel.close().sync(); // 关闭通道
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully(); // 关闭事件循环组
        }
    }

    // 初始化客户端连接
    private void init() {
        group = new NioEventLoopGroup(); // 赋值给成员变量
        try {
            Bootstrap bootstrap = new Bootstrap();
            clientHandler = new RpcClientHandler();

            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new RpcMessageDecoder());
                            pipeline.addLast(new RpcMessageEncoder());
                            pipeline.addLast(clientHandler);
                        }
                    });

            ChannelFuture future = bootstrap.connect(host, port).sync();
            channel = future.channel();
        } catch (InterruptedException e) {
            group.shutdownGracefully();
            throw new RuntimeException("Failed to connect to server", e);
        }
    }

    // 发送请求
    public Object sendRequest(RpcRequest request) throws Throwable {
        CompletableFuture<RpcResponse> future = new CompletableFuture<>();
        clientHandler.setFuture(request.getRequestId(), future);

        channel.writeAndFlush(request).addListener((ChannelFutureListener) f -> {
            if (!f.isSuccess()) {
                future.completeExceptionally(f.cause());
            }
        });

        RpcResponse response = future.get();
        if (response.getException() != null) {
            throw response.getException();
        }
        return response.getResult();
    }
}