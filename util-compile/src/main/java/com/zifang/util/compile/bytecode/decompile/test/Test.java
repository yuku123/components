package com.zifang.util.compile.bytecode.decompile.test;

import com.zifang.util.compile.bytecode.decompile.app.App;

public class Test {

    public static void main(String[] args) throws Exception {
        App app = new App("/Users/malcolmfeng/Documents/code/mine/Decomplier-Maven/src/main/java/org/example/test/", "FileClass");
        app.doDecomplie();
    }
}
