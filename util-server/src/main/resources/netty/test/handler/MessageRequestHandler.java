package com.zifang.util.server.netty.test.handler;

import com.zifang.util.core.demo.thirdpart.components.netty.test.Session;
import com.zifang.util.core.demo.thirdpart.components.netty.test.SessionUtil;
import com.zifang.util.core.demo.thirdpart.components.netty.test.packet.MessageRequestPacket;
import com.zifang.util.core.demo.thirdpart.components.netty.test.packet.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket) {
        // 1.拿到消息发送方的会话信息
        Session session = SessionUtil.getSession(ctx.channel());

        // 2.通过消息发送方的会话信息构造要发送的消息
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
//        messageResponsePacket.setFromUserId(session.getUserId());
//        messageResponsePacket.setFromUserName(session.getUserName());
        messageResponsePacket.setMessage(messageRequestPacket.getMessage());

        // 3.拿到消息接收方的 channel
//        Channel toUserChannel = SessionUtil.getChannel(messageRequestPacket.getToUserId());

        // 4.将消息发送给消息接收方
//        if (toUserChannel != null && SessionUtil.hasLogin(toUserChannel)) {
//            toUserChannel.writeAndFlush(messageResponsePacket);
//        } else {
//            System.err.println("[" + messageRequestPacket.getToUserId() + "] 不在线，发送失败!");
    }
}

