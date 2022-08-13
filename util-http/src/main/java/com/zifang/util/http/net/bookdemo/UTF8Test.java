package com.zifang.util.http.net.bookdemo;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class UTF8Test {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String pi = "\u03C0";
        byte[] data = pi.getBytes(StandardCharsets.UTF_8);
        for (byte x : data) {
            System.out.println(Integer.toHexString(x));
        }
    }

}
