package com.zifang.util.zex.leetcode;

public class A {

    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        int size = m+n;
        int[] r = new int[size];
        int num1Index = 0;
        int num2Index = 0;

        if(n == 0){
            return ;
        }

        if(m == 0){
            for(int i=0; i< n; i++){
                nums1[i] = nums2[i];
            }
            return ;
        }

        for(int i=0; i< size; i++){

            if(num1Index > m-1){
                r[i] = nums2[num2Index];
                num2Index = num2Index + 1;
                continue;
            }

            if(num2Index > n-1){
                r[i] = nums1[num1Index];
                num1Index = num1Index + 1;
                continue;
            }

            if(nums1[num1Index] < nums2[num2Index]){
                r[i] = nums1[num1Index];
                num1Index = num1Index +1;
            } else if(nums1[num1Index] == nums2[num2Index]){
                r[i] = nums1[num1Index];
                r[i+1] = nums2[num2Index];
                num1Index = num1Index+1;
                num2Index = num2Index + 1;
                i = i+1;
            } else if(nums1[num1Index] > nums2[num2Index]){
                r[i] = nums2[num2Index];
                num2Index = num2Index +1;
            }
        }

        for(int i=0; i< size; i++){
            nums1[i] = r[i];
        }
    }

    public static void main(String[] args) {
        merge(new int[]{1,2,3,0,0,0},3, new int[]{2,5,6},3);
    }
}
