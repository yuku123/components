package com.zifang.demo.thirdpart.components.netty.test.handler;

import com.zifang.demo.thirdpart.components.netty.test.Session;
import com.zifang.demo.thirdpart.components.netty.test.SessionUtil;
import com.zifang.demo.thirdpart.components.netty.test.packet.ControlRequestPacket;
import com.zifang.demo.thirdpart.components.netty.test.packet.ControlResponsePacket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ControlRequestHandler extends SimpleChannelInboundHandler<ControlRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ControlRequestPacket controlRequestPacket) {
        ControlResponsePacket controlResponsePacket = new ControlResponsePacket();

        Session session = SessionUtil.getSession(ctx.channel());
        controlResponsePacket.setUserId(controlRequestPacket.getUserId());
        controlResponsePacket.setUserTo(controlRequestPacket.getUserTo());
        // 3.拿到消息接收方的 channel
        Channel toUserChannel = SessionUtil.getChannel(controlRequestPacket.getUserTo());

        ctx.channel().writeAndFlush(controlResponsePacket);
    }
}
