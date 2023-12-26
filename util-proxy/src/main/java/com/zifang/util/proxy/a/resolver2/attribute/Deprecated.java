package com.zifang.util.proxy.a.resolver2.attribute;

import com.zifang.util.proxy.a.resolver2.readtype.U2;
import com.zifang.util.proxy.a.resolver2.readtype.U4;

import java.io.InputStream;

//Deprecated和Synthetic两个属性都属于标志类型的布尔属性，只存在有和没有的区别，没有属性值 的概念。
public class Deprecated extends AbstractAttribute {


    public Deprecated(U2 attributeNameIndex, U4 attributeLength) {
        super(attributeNameIndex, attributeLength);
    }

    @Override
    public void read(InputStream inputStream) {

    }
}
