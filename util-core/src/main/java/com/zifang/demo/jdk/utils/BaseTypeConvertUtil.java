package com.zifang.demo.jdk.utils;

public class BaseTypeConvertUtil {

    /**
     * long 转 byte数组
     */
    public static byte[] long2Bytes(long longa) {
        byte[] bytes = new byte[8];
        for (int i = 0; i < bytes.length; i++)
            bytes[i] = (byte) (long) (((longa) >> i * 8) & 0xff); // 移位和清零

        return bytes;
    }

    /**
     * byte数组 转 long
     */
    public static long bytes2Long(byte[] bytes) {
        long longa = 0;
        for (int i = 0; i < bytes.length; i++)
            longa += (long) ((bytes[i] & 0xff) << i * 8); // 移位和清零

        return longa;
    }

    public static void StringTObYTE(String[] args) {
        String str = "李强强";
        byte[] bytes = str.getBytes();
        // 打印字节数组
        System.out.println("'李强强'的字节数组为：");
        for (int i = 0; i < bytes.length; i++)
            System.out.print("\t" + bytes[i]);
    }

    public static void showint(String[] args) {
        System.out.println("17的十六进制： " + Integer.toHexString(17));
        System.out.println("17的八进制：     " + Integer.toOctalString(17));
        System.out.println("17的二进制：     " + Integer.toBinaryString(17));

        System.out.println(Integer.valueOf("11", 16));
        System.out.println(Integer.valueOf("21", 8));
        System.out.println(Integer.valueOf("00010001", 2));
    }

    /**
     * 1、(inta >> i * 8) & 0xff 移位 清零从左往右，按8位获取1字节。
     * 2、这里使用的是小端法。地位字节放在内存低地址端，即该值的起始地址。补充：32位中分大端模式（PPC）和小段端模式（x86）。
     *
     * @param args
     */
    public static void main(String[] args) {
        // 将我的学号转换成字节码
        byte[] bytes = BaseTypeConvertUtil.long2Bytes(1206010035);
        System.out.println(bytes[0] + " " + bytes[1] + " " + bytes[2] + " " + bytes[3]);
        // 字节码就可以转换回学号
        System.out.println(BaseTypeConvertUtil.bytes2Long(bytes));
    }

}
