package com.zifang.util.zex.sort;

import java.util.Arrays;

public class Sort02选择 {
    public static void main(String[] args) {
        int[] a = new int[]{4, 2, 6, 5, 1, 3};

        for (int i = 0; i < a.length - 1; i++) {
            int max = 0;
            int max_index = 0;
            for (int j = 0; j < a.length - i; j++) {
                if (a[j] > max) {
                    max = a[j];
                    max_index = j;
                }
            }
            a[max_index] = a[a.length - i - 1];
            a[a.length - i - 1] = max;
        }
        System.out.println(Arrays.toString(a));
    }
}
