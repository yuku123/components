package com.zifang.util.zex.sort;

public class Sort04快排 {

    public static void func(int[] a, int start, int end) {
        // 支点
        int point = (start + end) / 2;
        int point_value = a[point];

        int l = start;
        int r = end;
        while (l <= r) {
            while (point_value > a[l]) {
                l++;
            }
            while (point_value <= a[r]) {
                r++;
            }
            if (l <= r) {
                int temp = a[l];
                a[l] = a[r];
                a[r] = temp;
                l++;
                r--;
            }
        }
    }

    public static void main(String[] args) {
        int[] a = new int[]{4, 2, 6, 5, 1, 3, 10};
        func(a, 0, a.length - 1);
    }

}
