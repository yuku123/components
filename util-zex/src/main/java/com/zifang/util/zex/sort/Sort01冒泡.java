package com.zifang.util.zex.sort;

import java.util.Arrays;

/**
 * 每次拿两个进行比较，大的往后挪
 */
public class Sort01冒泡 {
    public static void main(String[] args) {
        int[] a = new int[]{4, 2, 6, 5, 1, 3};

        for (int i = 0; i < a.length - 1; i++) {
            for (int j = 0; j < a.length - i - 1; j++) {
                int a1 = a[j];
                int a2 = a[j + 1];
                if (a1 > a2) {
                    a[j + 1] = a1;
                    a[j] = a2;
                }
            }
        }
        System.out.println(Arrays.toString(a));
    }
}
