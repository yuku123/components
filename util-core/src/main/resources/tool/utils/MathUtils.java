package com.zifang.demo.temp.tool.utils;

/**
 * 数学工具类
 * 
 * @version 1.0
 */
public class MathUtils {


    /**
     * 获取[n, m]区间内的随机数
     * 
     * @param n
     * @param m
     * @return long
     */
    public static long getRandom(long n, long m) {
        if (n > m) {
            long temp = n;
            n = m;
            m = temp;
        }
        return (long) (n + Math.random() * (m - n + 1));
    }

    /**
     * 求两个数最大公约数
     * 
     * @param n
     * @param m
     * @return long
     */
    public static long commonDivisor(long n, long m) {
        // 辗转相除是用大的除以小的。如果n<m，第一次相当n与m值交换
        while (n % m != 0) {
            long temp = n % m;
            n = m;
            m = temp;
        }
        return m;
    }

    /**
     * 求两个数最小公倍数
     * 
     * @param n
     * @param m
     * @return long
     */
    public static long commonMultiple(long n, long m) {
        return n * m / commonDivisor(n, m);
    }

    /**
     * 求多个数最小公倍数
     * 
     * @param n
     * @return long
     */
    public static long commonMultiple(long... n) {
        long value = n[0];
        for (int i = 1; i < n.length; i++) {
            value = commonMultiple(value, n[i]);
        }
        return value;
    }
    
    public static void main(String[] args) {
	}
}