package com.zifang.util.zex.leetcode;

public class ss {
    public static int removeElement(int[] nums, int val) {
        int k = 0;
        int lastIndex = nums.length;
        for(int i = 0 ; i< nums.length && i < lastIndex ; i++){
            if(nums[i]==val){
                for(int t = nums.length-1; t > 0 ; t--){
                    lastIndex = t;
                    if(nums[t]!=val){
                        k = k+1;
                        int temp = nums[i];
                        nums[i] = nums[t];
                        nums[t] = temp;
                        break;
                    }
                }
            } else {
                k = k+1;
            }
        }
        return k;
    }

    public static void main(String[] args) {
        removeElement(new int[]{2},3);
    }
}
