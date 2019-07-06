package com.zifang.demo.thirdpart.components.netty.im.ch9;


import static com.zifang.demo.thirdpart.components.netty.im.ch9.Command.MESSAGE_RESPONSE;

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
