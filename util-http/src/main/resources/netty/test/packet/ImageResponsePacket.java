package com.zifang.util.server.netty.test.packet;


import static com.zifang.util.core.demo.thirdpart.components.netty.im.ch9.Command.IMAGE_RESPONSE;

public class ImageResponsePacket extends Packet {

    private String message;

    public byte[] getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(byte[] bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    private byte[] bufferedImage;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public Byte getCommand() {

        return IMAGE_RESPONSE;
    }
}
