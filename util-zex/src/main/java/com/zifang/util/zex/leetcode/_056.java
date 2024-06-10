package com.zifang.util.zex.leetcode;

import java.util.Arrays;
import java.util.Comparator;

public class _056 {
    public static void main(String[] args) {

        Integer[] integers = new Integer[]{1,-1,3};
        Arrays.sort(integers, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });

        System.out.println();
    }
}
