package com.zifang.util.server.netty.test.packet;


import static com.zifang.util.core.demo.thirdpart.components.netty.im.ch9.Command.MESSAGE_RESPONSE;

public class MessageResponsePacket extends Packet {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Byte getCommand() {

        return MESSAGE_RESPONSE;
    }
}
