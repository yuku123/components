package com.zifang.util.compile.bytecode.resolver2.inter;


import com.zifang.util.compile.bytecode.resolver2.readtype.U2;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 接口信息
 */
public class InterfaceIndex {
    public U2 length;
    public List<Interface> list = new ArrayList<Interface>();

    public InterfaceIndex(InputStream stream) {
        length = U2.read(stream);
    }
}
