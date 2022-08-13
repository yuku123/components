package com.zifang.util.server.netty.im.ch8;

import com.alibaba.fastjson.annotation.JSONField;

public abstract class Packet {


    @JSONField(deserialize = false, serialize = false)
    private Byte version = 1;


    @JSONField(serialize = false)
    public abstract Byte getCommand();


    public Byte getVersion() {
        return version;
    }

    public void setVersion(Byte version) {
        this.version = version;
    }

}
