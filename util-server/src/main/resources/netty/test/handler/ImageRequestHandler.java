package com.zifang.util.server.netty.test.handler;

import com.zifang.util.core.demo.thirdpart.components.netty.test.packet.ImageRequestPacket;
import com.zifang.util.core.demo.thirdpart.components.netty.test.packet.ImageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageRequestHandler extends SimpleChannelInboundHandler<ImageRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ImageRequestPacket imageRequestPacket) throws Exception {
        System.out.println("ImageRequestHandler------");
        ImageResponsePacket imageResponsePacket = new ImageResponsePacket();
        Robot robot = new Robot();
        //设置Robot产生一个动作后的休眠时间,否则执行过快
        robot.setAutoDelay(1000);
        //获取屏幕分辨率
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println(d);
        //以屏幕的尺寸创建个矩形
        Rectangle screenRect = new Rectangle(d);
        //截图（截取整个屏幕图片）
        BufferedImage bufferedImage =  robot.createScreenCapture(screenRect);
        //imageResponsePacket.setBufferedImage(bufferedImage);
//
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage,"png",out);
        imageResponsePacket.setBufferedImage(out.toByteArray());
        channelHandlerContext.channel().writeAndFlush(imageResponsePacket);

        Thread thread = new Thread(){
            @Override
            public void run(){
                while(true){
                    ImageResponsePacket imageResponsePacket = new ImageResponsePacket();
                    Robot robot = null;
                    try {
                        robot = new Robot();
                    } catch (AWTException e) {
                        e.printStackTrace();
                    }
                    //设置Robot产生一个动作后的休眠时间,否则执行过快
                    robot.setAutoDelay(1000);
                    //获取屏幕分辨率
                    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                    System.out.println(d);
                    //以屏幕的尺寸创建个矩形
                    Rectangle screenRect = new Rectangle(d);
                    //截图（截取整个屏幕图片）
                    BufferedImage bufferedImage =  robot.createScreenCapture(screenRect);
                    //imageResponsePacket.setBufferedImage(bufferedImage);
//
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    try {
                        ImageIO.write(bufferedImage,"png",out);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageResponsePacket.setBufferedImage(out.toByteArray());
                    channelHandlerContext.channel().writeAndFlush(imageResponsePacket);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                }

            }
        };

        thread.start();
    }
}
