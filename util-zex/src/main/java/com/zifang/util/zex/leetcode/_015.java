package com.zifang.util.zex.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class _015 {
    public List<List<Integer>> threeSum(int[] nums) {

        List<List<Integer>> r = new ArrayList<>();
        if(nums.length < 3){
            return r;
        }

        Arrays.sort(nums);

        int start = 0;
        int end = nums.length -1;

        while(true){
            // 间隔内无数据，或者起始值大于0 则返回
            if(end - start <1 || nums[start]>0){
                break;
            }

            // 开始看间隔内的所有解，可以执行二分，目前暂时遍历
            int tempSum = nums[start] + nums[end];
            for(int i = start+1; i< end;i++){
                if(tempSum + nums[i] == 0){
                    r.add(Arrays.asList(nums[start], nums[i],nums[end]));
                    break;
                }

                if(tempSum + nums[i] > 0){
                    break;
                }

            }

            while(true && start < nums.length-1){
                start = start +1;
                if(nums[start] != nums[start -1]){
                    break;
                }
            }

            while(true && end > 1){
                end = end -1;
                if(nums[end] != nums[end -1]){
                    break;
                }
            }
        }

        return r;
    }

    public static void main(String[] args) {
        new _015().threeSum(new int[]{-1,0,1,2,-1,-4});
    }
}
