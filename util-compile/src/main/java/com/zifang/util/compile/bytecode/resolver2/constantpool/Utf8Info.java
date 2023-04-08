package com.zifang.util.compile.bytecode.resolver2.constantpool;

import com.zifang.util.compile.bytecode.resolver2.readtype.U2;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class Utf8Info extends AbstractConstantPool {

    private short length;
    private byte[] bytes;
    private String bytesValue;

    public Utf8Info(byte tag) {
        super(tag);
    }

    @Override
    public void read(InputStream in) {
        U2 u2 = U2.read(in);
        this.length = u2.getValue();
        this.bytes = new byte[this.length];
        try {
            in.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //convertMUTF8(bytes);
        this.bytesValue = getValue();
    }


    public String getValue() {
        String ret = null;
        try {
            ret = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public String toString() {
        return "Utf8Info{" +
                "bytesValue='" + bytesValue + '\'' +
                '}';
    }
}
