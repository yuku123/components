package com.zifang.util.compile.bytecode.resolver.parser.struct;

public interface AccessFlag {

    int PUBLIC = 0x0001;

    int FINAL = 0x0010;

    int SUPER = 0x0020;

    int INTERFACE = 0x0200;

    int ABSTRACT = 0x0400;

    int SYNTHETIC = 0x1000;

    int ANNOTATION = 0x2000;

    int ENUM = 0x4000;
}
