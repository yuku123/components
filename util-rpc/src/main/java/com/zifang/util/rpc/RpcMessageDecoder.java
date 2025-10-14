package com.zifang.util.rpc;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

public class RpcMessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 简单起见，这里使用Java序列化，实际项目中可替换为Protobuf等
        if (in.readableBytes() < 4) {
            return;
        }

        in.markReaderIndex();
        // 读取魔数
        byte[] magicNumber = new byte[3];
        in.readBytes(magicNumber);
        if (!new String(magicNumber).equals(RpcConstants.MAGIC_NUMBER)) {
            throw new IllegalArgumentException("Invalid magic number");
        }

        // 读取版本号
        byte version = in.readByte();
        if (version != RpcConstants.VERSION) {
            throw new IllegalArgumentException("Invalid version");
        }

        // 读取消息长度
        int length = in.readInt();
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        // 读取消息内容
        byte[] content = new byte[length];
        in.readBytes(content);

        // 反序列化
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(content));
        Object obj = ois.readObject();
        out.add(obj);
    }
}