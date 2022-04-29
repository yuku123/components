package com.zifang.util.compile.bytecode.simple2.readtype;

import java.io.IOException;
import java.io.InputStream;

public class U2 {
    public short value;

    public U2(short value) {
        this.value = value;
    }

    public static U2 read(InputStream stream){
        byte[] bytes=new byte[2];
        try {
            stream.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        short value=0;
        for (int i = 0; i < 2; i++) {
            value <<= 8;
            value |= bytes[i] & 0xff;
        }

        U2 u2=new U2(value);
        return u2;
    }

    public short getValue() {
        return value;
    }


}
