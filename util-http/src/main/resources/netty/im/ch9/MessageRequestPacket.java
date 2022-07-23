package com.zifang.util.server.netty.im.ch9;

import static com.zifang.util.core.demo.thirdpart.components.netty.im.ch9.Command.MESSAGE_REQUEST;

public class MessageRequestPacket extends Packet {

    private String message;

    public MessageRequestPacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}
