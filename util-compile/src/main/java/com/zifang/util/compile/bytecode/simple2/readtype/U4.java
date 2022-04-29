package com.zifang.util.compile.bytecode.simple2.readtype;

import java.io.IOException;
import java.io.InputStream;


public class U4 {

    public int value;
    public byte[] bytes;

    public U4(int value,byte[] bytes) {
        this.value = value;
        this.bytes=bytes;
    }

    public static U4 read(InputStream inputStream) {
        byte[] bytes = new byte[4];
        try {
            inputStream.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int value = 0;
        //4字节数据转成int类型数据需要做的位运算转换.
        for (int i = 0; i < 4; i++) {
            value <<= 8;
            int temp=bytes[i] & 0xFF;
            value = value|temp;        //想保持二进制补码的一致性。进行 & 运算； & 运算是: 两个为1则为1；
                                       // | 运算：有一个为1 则为1；
        }
        U4 u4 = new U4(value,bytes);
        return u4;
    }

    public int getValue() {
        return value;
    }

}
