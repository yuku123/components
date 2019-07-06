package com.zifang.util.core.demo.temp.tool.utils;

/**
 * ��ѧ������
 * 
 * @version 1.0
 */
public class MathUtils {


    /**
     * ��ȡ[n, m]�����ڵ������
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
     * �����������Լ��
     * 
     * @param n
     * @param m
     * @return long
     */
    public static long commonDivisor(long n, long m) {
        // շת������ô�ĳ���С�ġ����n<m����һ���൱n��mֵ����
        while (n % m != 0) {
            long temp = n % m;
            n = m;
            m = temp;
        }
        return m;
    }

    /**
     * ����������С������
     * 
     * @param n
     * @param m
     * @return long
     */
    public static long commonMultiple(long n, long m) {
        return n * m / commonDivisor(n, m);
    }

    /**
     * ��������С������
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