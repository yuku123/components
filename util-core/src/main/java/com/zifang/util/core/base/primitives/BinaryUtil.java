package com.zifang.util.core.base.primitives;

/**
 * 二进制管理器
 *
 * */
public class BinaryUtil {

    public static void showint() {
        System.out.println("17的十六进制： " + Integer.toHexString(17));
        System.out.println("17的八进制：     " + Integer.toOctalString(17));
        System.out.println("17的二进制：     " + Integer.toBinaryString(17));

        System.out.println(Integer.valueOf("11", 16));
        System.out.println(Integer.valueOf("21", 8));
        System.out.println(Integer.valueOf("00010001", 2));
    }

    public static void main(String[] args) {
        showint();
    }
}
