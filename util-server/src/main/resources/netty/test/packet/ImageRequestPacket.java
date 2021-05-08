package com.zifang.util.server.netty.test.packet;

import static com.zifang.util.core.demo.thirdpart.components.netty.im.ch9.Command.IMAGE_REQUEST;

public class ImageRequestPacket extends Packet {
    private String userId;

    private String username;

    private String password;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Byte getCommand() {

        return IMAGE_REQUEST;
    }

    public Byte getVersion() {
        return version;
    }
}
