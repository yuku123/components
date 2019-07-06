package com.zifang.util.core.demo.thirdpart.components.netty.im.ch9;


import static com.zifang.util.core.demo.thirdpart.components.netty.im.ch9.Command.LOGIN_RESPONSE;

public class LoginResponsePacket extends Packet {
    private boolean success;

    private String reason;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public Byte getCommand() {
        return LOGIN_RESPONSE;
    }

    public void setVersion(Byte version){
        this.version = version;
    }
}
