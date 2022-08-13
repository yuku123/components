package com.zifang.util.server.netty.test.packet;

import static com.zifang.util.core.demo.thirdpart.components.netty.im.ch9.Command.MESSAGE_REQUEST;

public class MessageRequestPacket extends Packet {

    private String message;
    private String userTo;

    public String getUserTo() {
        return userTo;
    }

    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

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
