package com.zifang.util.proxy.a.resolver2.field;

import com.zifang.util.proxy.a.resolver2.readtype.U2;

import java.util.ArrayList;
import java.util.List;


public class FieldInfo {
    public U2 length;
    public List<FieldTable> list = new ArrayList();

    public FieldInfo(U2 length) {
        this.length = length;
    }
}
