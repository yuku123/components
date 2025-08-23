package com.zifang.util.core.lang;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class Base64Util {

    private static final Decoder DECODER = Base64.getDecoder();
    private static final Encoder ENCODER = Base64.getEncoder();

    public static String encode(String text) {
        return encode(text, Charset.defaultCharset());
    }

    public static String encode(byte[] text) {
        return encode(text);
    }

    public static String encode(String text, Charset charset) {
        return ENCODER.encodeToString(text.getBytes(charset));
    }
    public static byte[] encodeByte(String text, Charset charset) {
        return ENCODER.encode(text.getBytes(charset));
    }

    public static byte[] encodeByte(String text) {
        return encodeByte(text, Charset.defaultCharset());
    }

    public static byte[] encodeByte(byte[] text) {
        return ENCODER.encode(text);
    }

    public static String decode(String src) {
        return decode(src.getBytes(Charset.defaultCharset()));
    }

    public static String decode(byte[] src) {
        return decode(src, Charset.defaultCharset());
    }

    public static String decode(byte[] src, Charset charset) {
        return new String(decodeByte(src), charset);
    }
    public static byte[] decodeByte(byte[] src) {
        return DECODER.decode(src);
    }

    public static byte[] decodeByte(String src) {
        return DECODER.decode(src.getBytes(Charset.defaultCharset()));
    }
}
