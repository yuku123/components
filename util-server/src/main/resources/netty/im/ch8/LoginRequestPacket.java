package com.zifang.util.server.netty.im.ch8;

import static com.zifang.util.core.demo.thirdpart.components.netty.im.ch8.Command.LOGIN_REQUEST;

public class LoginRequestPacket extends Packet {
    private String userId;
    private String userName;
    private String password;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Byte getCommand() {
        return LOGIN_REQUEST;
    }
}
