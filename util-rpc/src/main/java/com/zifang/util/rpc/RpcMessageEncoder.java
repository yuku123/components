package com.zifang.util.rpc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class RpcMessageEncoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        // 写入魔数
        out.writeBytes(RpcConstants.MAGIC_NUMBER.getBytes());
        // 写入版本号
        out.writeByte(RpcConstants.VERSION);

        // 序列化消息
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(msg);
        byte[] content = bos.toByteArray();

        // 写入消息长度和内容
        out.writeInt(content.length);
        out.writeBytes(content);
    }
}