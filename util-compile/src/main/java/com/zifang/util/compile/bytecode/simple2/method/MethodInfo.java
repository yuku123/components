package com.zifang.util.compile.bytecode.simple2.method;

import com.zifang.util.compile.bytecode.simple2.readtype.U2;

import java.util.ArrayList;
import java.util.List;

/**
 * 方法信息,未完成
 */

public class MethodInfo {

    public U2 length;

    public List<MethodTable> list = new ArrayList<MethodTable>();

    public MethodInfo(U2 length) {
        this.length = length;
    }
}
