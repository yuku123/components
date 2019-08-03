package com.zifang.util.server.netty.test.handler;

import com.zifang.util.core.demo.thirdpart.components.netty.test.packet.ImageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import javax.swing.*;
import java.awt.*;

public class ImageResponseHandler extends SimpleChannelInboundHandler<ImageResponsePacket> {

    public static Frame frame = new Frame();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ImageResponsePacket imageResponsePacket) throws Exception {

        byte[] bufferedImage = imageResponsePacket.getBufferedImage();
        frame.refresh(bufferedImage);
//        System.out.println(bufferedImage.toString());
//        System.out.println("client 接收到图片数据了");
//
//        JFrame jFrame = new JFrame();
//        JPanel jp = new JPanel();
//        JLabel jLabel = new JLabel();
//        jLabel.setIcon(new ImageIcon(bufferedImage));
//        jp.add(jLabel);
//        jFrame.add(jp);
//        jFrame.setSize(new Dimension(500,500));
//        jFrame.setVisible(true);
//        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}

class Frame {
    JFrame jFrame = new JFrame();
    JPanel jp = new JPanel();
    JLabel jLabel = new JLabel();

    Frame(){
        jp.add(jLabel);
        jFrame.add(jp);
        jFrame.setSize(new Dimension(500,500));
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void refresh(byte[] bufferedImage){
        jLabel.setIcon(new ImageIcon(bufferedImage));
        jFrame.validate();
        jFrame.repaint();
    }
}
