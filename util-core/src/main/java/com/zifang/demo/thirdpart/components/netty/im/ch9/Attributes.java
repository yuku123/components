package com.zifang.demo.thirdpart.components.netty.im.ch9;

import io.netty.util.AttributeKey;

public interface Attributes {
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
}
