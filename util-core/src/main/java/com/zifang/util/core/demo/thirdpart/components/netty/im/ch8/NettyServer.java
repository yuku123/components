package com.zifang.util.core.demo.thirdpart.components.netty.im.ch8;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyServer {
    public static void main(String[] args) {
        //这两个对象可以看做是传统IO编程模型的两大线程组
        //bossGroup表示监听端口，accept 新连接的线程组，workerGroup表示处理每一条连接的数据读写的线程组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        //引导类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup, workerGroup)                        //线程模型
                .channel(NioServerSocketChannel.class)                //指定 IO 模型
                .childHandler(new ChannelInitializer<NioSocketChannel>() { //业务处理逻辑
                    protected void initChannel(NioSocketChannel ch) {
                        //服务端读写逻辑
                        ch.pipeline().addLast(new ServerHandler());
                    }
                });

        serverBootstrap.bind(8000);

        //
//        serverBootstrap.bind(8000).addListener(new GenericFutureListener<Future<? super Void>>() {
//            public void operationComplete(Future<? super Void> future) {
//                if (future.isSuccess()) {
//                    System.out.println("端口绑定成功!");
//                } else {
//                    System.err.println("端口绑定失败!");
//                }
//            }
//        });
    }
}