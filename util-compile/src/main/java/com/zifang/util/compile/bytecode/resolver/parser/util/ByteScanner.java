package com.zifang.util.compile.bytecode.a.parser.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zifang
 */
public class ByteScanner {

    private InputStream inputStream;

    public ByteScanner(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public int readToInteger(int size) {
        byte[] b = new byte[size];
        try {
            this.inputStream.read(b);
            if (size == 1) {
                return b[0] & 0x000000ff;
            } else if (size == 2) {
                return b[0] << 8 & 0x0000ff00 | b[1] & 0x000000ff;
            } else if (size == 4) {
                return b[0] << 24 & 0xff000000 | b[1] << 16 & 0x00ff0000 | b[2] << 8 & 0x0000ff00 | b[3] & 0x000000ff;
            } else {
                return -1;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public long readToLong() {
        byte[] bytes = new byte[8];
        try {
            this.inputStream.read(bytes);
            return bytes[0] << 56 & 0xff00000000000000L | bytes[1] << 48 & 0x00ff000000000000L
                    | bytes[2] << 40 & 0x0000ff0000000000L | bytes[3] << 32 & 0x000000ff00000000L
                    | bytes[4] << 24 & 0x00000000ff000000L | bytes[5] << 16 & 0x0000000000ff0000L
                    | bytes[6] << 8 & 0x0000000000ff00L | bytes[7] & 0x00000000000000ffL;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public String readToString(int size) {
        byte[] b = new byte[size];
        try {
            this.inputStream.read(b);
            return new String(b).intern();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
