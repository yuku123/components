package com.zifang.demo.thirdpart.components.netty.im.ch9;

import com.alibaba.fastjson.annotation.JSONField;

public abstract class Packet {
    /**
     * 协议版本
     */
    @JSONField(deserialize = false, serialize = false)
    public Byte version = 1;


    @JSONField(serialize = false)
    public abstract Byte getCommand();

    public Byte getVersion(){
        return this.version;
    }
}
