package com.zifang.util.core.base.primitives;

public class Arrays {


    /**
     * long 转 byte数组
     */
    public static byte[] long2Bytes(long longa) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < bytes.length; i++)
            bytes[i] = (byte) (long) (((longa) >> i * 8) & 0xff); // 移位和清零
        return bytes;
    }

    public static void StringTObYTE(String[] args) {
        String str = "李强强";
        byte[] bytes = str.getBytes();
        // 打印字节数组
        System.out.println("'李强强'的字节数组为：");
        for (int i = 0; i < bytes.length; i++)
            System.out.print("\t" + bytes[i]);
    }


}
