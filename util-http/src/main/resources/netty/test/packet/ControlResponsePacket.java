package com.zifang.util.server.netty.test.packet;


import static com.zifang.util.core.demo.thirdpart.components.netty.im.ch9.Command.CONTROL_RESPONSE;

public class ControlResponsePacket extends Packet {

    private String message;
    private byte[] bufferedImage;
    private String userId;
    private String userTo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserTo() {
        return userTo;
    }

    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

    public byte[] getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(byte[] bufferedImage) {
        this.bufferedImage = bufferedImage;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public Byte getCommand() {

        return CONTROL_RESPONSE;
    }
}
