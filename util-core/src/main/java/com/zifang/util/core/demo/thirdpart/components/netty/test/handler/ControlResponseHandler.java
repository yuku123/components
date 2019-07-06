package com.zifang.util.core.demo.thirdpart.components.netty.test.handler;

import com.zifang.util.core.demo.thirdpart.components.netty.test.Session;
import com.zifang.util.core.demo.thirdpart.components.netty.test.SessionUtil;
import com.zifang.util.core.demo.thirdpart.components.netty.test.packet.ControlResponsePacket;
import com.zifang.util.core.demo.thirdpart.components.netty.test.packet.ImageResponsePacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ControlResponseHandler extends SimpleChannelInboundHandler<ControlResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ControlResponsePacket controlResponsePacket) {
        Session session = SessionUtil.getSession(ctx.channel());
        Channel toUserChannel = SessionUtil.getChannel(controlResponsePacket.getUserTo());


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

        ctx.channel().writeAndFlush(imageResponsePacket);
    }
}
