package com.zifang.util.zex.leetcode;

public class _042 {
    public int trap(int[] height) {

        int base = 0;
        int score = 0;
        for(int i = 0; i< height.length -1; i++){
            if(i == 0){
                base = height[i];
                continue;
            }

            if(height[i] >= base){
                base = height[i];
                continue;
            }

            // 开始计算周期
            if(height[i] < base){

                int loopStart = i-1;
                int loopEnd = 0;

                int maxMax = 0;
                int maxMaxIndex = 0;

                int minMax = 0;
                int minMaxIndex = 0;

                for(int j = i; j < height.length; j++){
                    if(height[j] >= height[j-1] && height[j] >= maxMax){
                        maxMax = height[j];
                        maxMaxIndex = j;
                    }
                    if(maxMax > height[loopStart]){
                        break;
                    }
                }

                // 计算区间值
                loopEnd = maxMaxIndex;
                if(loopEnd > loopStart){
                    int s1= height[loopStart];
                    int s2 = height[loopEnd];
                    int min = Math.min(s1, s2);
                    for(int k = loopStart; k < loopEnd+1 ; k++){
                        if(min > height[k]){
                            score = score + min - height[k];
                        }
                    }
                    i = loopEnd-1 ;
                    base = height[i];
                }
            }
        }
        return score;
    }

    public static void main(String[] args) {
        new _042().trap(new int[]{5,4,1,2});
    }
}
