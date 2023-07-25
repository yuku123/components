package com.zifang.util.core.lang.primitive;


import com.zifang.util.core.util.StringUtil;

/**
 * 提供位运算操作的工具类
 */
public class Bits {

    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * 得到int 与 long 二进制字符串
     */
    public static String binaryStr(Number number) {
        if (number == null) {
            throw new NullPointerException();
        }
        if (number instanceof Integer) {
            return StringUtil.leftPad(Integer.toBinaryString(number.intValue()), Integer.SIZE, '0');
        } else if (number instanceof Long) {
            return StringUtil.leftPad(Long.toBinaryString(number.longValue()), Long.SIZE, '0');
        } else {
            return String.valueOf(number);
        }
    }

    /**
     * 判断是偶数
     */
    public static boolean isOdd(int i) {
        return (abs(i) & 1) == 0;
    }

    /**
     * 判断是奇数
     */
    public static boolean isEven(int i) {
        return !isOdd(i);
    }

    /**
     * 获得平均值
     */
    public static int avg(int x, int y) {
        return (x & y) + ((x ^ y) >> 1);
    }

    /**
     * 是否是2的次方
     */
    public static boolean isPowFrom2(int x) {
        return ((x & (x - 1)) == 0) && (x != 0);
    }

    /**
     * 求绝对值
     */
    public static int abs(int x) {
        int y = x >> 31;
        return (x ^ y) - y;
    }

    /**
     * 快速取模
     */
    public static int mod(int x, int mod) {
        if (isPowFrom2(mod)) {
            return x & (mod - 1);
        } else {
            return x % mod;
        }
    }

    /**
     * 获得n以下，最接近cap的2的倍数
     */
    public static int multipleLess(int n) {
        n |= n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        return (n + 1) >> 1;
    }

    /**
     * 获得cap以上，最接近cap的2的倍数
     */
    public static int multipleMore(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    /**
     * 从byte里获取特定位数上的值
     */
    public static int at(int i, int k) {
        return 1; // todo
    }
}
