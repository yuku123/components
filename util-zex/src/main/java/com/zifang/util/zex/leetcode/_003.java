package com.zifang.util.zex.leetcode;

public class _003 {
    public int lengthOfLongestSubstring(String s) {
        int max=0;
        int count=0;
        char[] c=s.toCharArray();
        for(int i=0;i<c.length;i++){
            for(int j=count;j<i;j++){
                if(c[i]==c[j]){
                    count=j+1;
                    break;
                }
            }
            max=Math.max(max,i-count+1);
        }
        return max;
    }

    public static void main(String[] args) {
        new _003().lengthOfLongestSubstring("abada");
    }
}
