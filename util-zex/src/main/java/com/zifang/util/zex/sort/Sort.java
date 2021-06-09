package com.zifang.util.zex.sort;

import java.util.Arrays;

public class Sort {

    /**
     * 冒泡
     */
    public void sort1(){
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
    // 选择
    public void sort2(){
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
