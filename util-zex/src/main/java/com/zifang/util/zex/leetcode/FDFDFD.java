package com.zifang.util.zex.leetcode;

public class FDFDFD {

    public static int maxProfit(int[] prices) {


        int min = prices[0];
        int max = prices[0];
        int last = 0;
        int p = 0;


        if(prices.length == 2){
            if(prices[0] < prices[1]){
                return prices[1]-prices[0];
            } else {
                return 0;
            }
        }


        // 先以0号位开始找到最大的，最大的范围内找到最小的
        // 一组过后找下一组最小最大
        for(int i=1; i< prices.length; i++){
            if(i + 1 < prices.length && prices[i] < min && prices[i+1] > prices[i]){
                min = prices[i];

                for(int j = i + 1; j < prices.length; j++){
                    if(j== i + 1){
                        max = prices[j];
                    } else {
                        if(max < prices[j]){
                            max = prices[j];
                            i= j;
                            if(p < max - min){
                                p = max - min;
                            }
                        }
                    }
                }
            }
        }
        return p ;
    }

    public static void main(String[] args) {
        maxProfit(new int[]{1,2,3});
    }
}
