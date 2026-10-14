package com.zifang.util.bc.bytecode.model.readtype;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 大端字节流读取器
 * <p>
 * {@link InputStream#read(byte[])} 不保证读满，class 文件格式要求按固定宽度精确读取，
 * 短读会让后续所有字段整体错位。本类统一处理「读满 n 字节」与越界检测，
 * 供 readtype 与各模型类的读取路径共用。
 */
public class ByteReader {

    private final InputStream inputStream;

    public ByteReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * 精确读取 n 字节
     *
     * @param n 字节数
     * @return 读到的字节数组
     */
    public byte[] readFully(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("读取长度为负: " + n);
        }
        byte[] buf = new byte[n];
        int off = 0;
        try {
            while (off < n) {
                int r = inputStream.read(buf, off, n - off);
                if (r < 0) {
                    throw new EOFException("class 文件提前结束: 需要 " + n + " 字节，已读到 " + off);
                }
                off += r;
            }
        } catch (IOException e) {
            throw new ByteCodeReadException("读取字节码失败，剩余 " + (n - off) + " 字节未读到", e);
        }
        return buf;
    }

    public U1 readU1() {
        return new U1(readFully(1)[0]);
    }

    public U2 readU2() {
        return U2.of(readFully(2));
    }

    public U4 readU4() {
        return U4.of(readFully(4));
    }

    public U8 readU8() {
        return U8.of(readFully(8));
    }

    /**
     * 按 attribute_length 跳过未知属性内容，保证流不发生错位
     *
     * @param n 字节数
     */
    public void skipFully(int n) {
        long remaining = n;
        try {
            while (remaining > 0) {
                long skipped = inputStream.skip(remaining);
                if (skipped > 0) {
                    remaining -= skipped;
                    continue;
                }
                if (inputStream.read() == -1) {
                    throw new EOFException("跳过属性内容时提前结束，剩余 " + remaining);
                }
                remaining--;
            }
        } catch (IOException e) {
            throw new ByteCodeReadException("跳过属性内容失败，剩余 " + remaining + " 字节", e);
        }
    }
}
