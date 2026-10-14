package com.zifang.util.bc.bytecode.model.readtype;

/**
 * 改进型 UTF-8（Modified UTF-8）编解码
 * <p>
 * class 文件的 CONSTANT_Utf8_info 用的是 JVM 规范的 MUTF-8 而非标准 UTF-8，
 * 两者有三点差异，任何一个搞错都会让含中文或补充平面字符的类无法加载：
 * <ol>
 *   <li>U+0000 编成两字节 C0 80，而不是标准 UTF-8 的单字节 00；</li>
 *   <li>U+0001-U+007F 之外的 BMP 字符一律编成三字节，标准 UTF-8 的两字节形式不出现；</li>
 *   <li>补充平面字符按 UTF-16 代理对各编三字节（六个字节），而不是标准 UTF-8 的四字节。</li>
 * </ol>
 */
public final class Mutf8 {

    private Mutf8() {
    }

    /**
     * 解码为 Java String
     *
     * @param bytes MUTF-8 字节
     * @param offset 起始下标
     * @param length 长度
     * @return String类型返回值
     */
    public static String decode(byte[] bytes, int offset, int length) {
        char[] out = new char[length];
        int n = 0;
        int i = offset;
        int end = offset + length;
        while (i < end) {
            int b1 = bytes[i++] & 0xFF;
            if (b1 < 0x80) {
                out[n++] = (char) b1;
            } else if ((b1 & 0xE0) == 0xC0) {
                int b2 = bytes[i++] & 0xFF;
                out[n++] = (char) (((b1 & 0x1F) << 6) | (b2 & 0x3F));
            } else if ((b1 & 0xF0) == 0xE0) {
                int b2 = bytes[i++] & 0xFF;
                int b3 = bytes[i++] & 0xFF;
                out[n++] = (char) (((b1 & 0x0F) << 12) | ((b2 & 0x3F) << 6) | (b3 & 0x3F));
            } else {
                throw new IllegalArgumentException("非法的 MUTF-8 首字节: 0x" + Integer.toHexString(b1));
            }
        }
        return new String(out, 0, n);
    }

    /**
     * 编码为 MUTF-8 字节
     *
     * @param s 待编码字符串
     * @return byte[]类型返回值
     */
    public static byte[] encode(String s) {
        byte[] buf = new byte[s.length() * 3 + 4];
        int n = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0x0001 && c <= 0x007F) {
                buf[n++] = (byte) c;
            } else if (c <= 0x07FF) {
                buf[n++] = (byte) (0xC0 | (c >> 6));
                buf[n++] = (byte) (0x80 | (c & 0x3F));
            } else {
                buf[n++] = (byte) (0xE0 | (c >> 12));
                buf[n++] = (byte) (0x80 | ((c >> 6) & 0x3F));
                buf[n++] = (byte) (0x80 | (c & 0x3F));
            }
        }
        byte[] out = new byte[n];
        System.arraycopy(buf, 0, out, 0, n);
        return out;
    }
}
