package com.zifang.util.server.netty.test;

import com.zifang.util.server.netty.test.handler.ImageResponseHandler;
import com.zifang.util.server.netty.test.handler.LoginResponseHandler;
import com.zifang.util.server.netty.test.handler.MessageResponseHandler;
import com.zifang.util.server.netty.test.packet.ControlRequestPacket;
import com.zifang.util.server.netty.test.packet.ImageRequestPacket;
import com.zifang.util.server.netty.test.packet.LoginRequestPacket;
import com.zifang.util.server.netty.test.packet.MessageRequestPacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author 闪电侠
 */
public class NettyClient {
    private static final int MAX_RETRY = 5;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8000;


    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 7, 4));
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new LoginResponseHandler());
                        ch.pipeline().addLast(new MessageResponseHandler());
                        ch.pipeline().addLast(new ImageResponseHandler());
                        ch.pipeline().addLast(new PacketEncoder());
                    }
                });

        connect(bootstrap, HOST, PORT, MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 连接成功，启动控制台线程……");
                Channel channel = ((ChannelFuture) future).channel();
                startConsoleThread(channel);
            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit
                        .SECONDS);
            }
        });
    }

    private static void startConsoleThread(Channel channel) {
        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (LoginUtil.hasLogin(channel)) {
                    System.out.println("输入消息发送至服务端: ");
                    Scanner sc = new Scanner(System.in);
                    String line = sc.nextLine();
                    if (line.equals("login")) {
                        String userId = "u" + new Random().nextInt(10);
                        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
                        loginRequestPacket.setUserId(userId);
                        loginRequestPacket.setPassword("password:" + userId);
                        loginRequestPacket.setUsername("username:" + userId);
                        channel.writeAndFlush(loginRequestPacket);
                    } else if (line.equals("image")) {
                        channel.writeAndFlush(new ImageRequestPacket());
                    } else if (line.contains("message")) {
                        //message u3 aaaaa
                        String userTo = line.split(" ")[1];
                        String message = line.split(" ")[2];

                        MessageRequestPacket messageRequestPacket = new MessageRequestPacket(message);
                        messageRequestPacket.setUserTo(userTo);

                        channel.writeAndFlush(messageRequestPacket);

                    } else if (line.contains("control")) {
                        //control usmyself usto
                        String userId = line.split(" ")[1];
                        String userTo = line.split(" ")[2];
                        ControlRequestPacket controlRequestPacket = new ControlRequestPacket();
                        controlRequestPacket.setUserTo(userTo);
                        controlRequestPacket.setUserId(userId);
                        channel.writeAndFlush(controlRequestPacket);
                    }
                }
            }
        }).start();
    }
}
