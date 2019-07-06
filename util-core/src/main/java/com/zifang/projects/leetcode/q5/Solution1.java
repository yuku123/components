package com.zifang.projects.leetcode.q5;

public class Solution1 {

    public String longestPalindrome(String s) {

        String result="";

        System.out.println(s.length());
        for(int i = 0 ; i < s.length()+1;i++){
            for(int j = i ; j < s.length()+1;j++){
                System.out.println(i+":"+j);
                String s_zheng = s.substring(i,j);
                String s_fan = new StringBuffer(s_zheng).reverse().toString();
                if(s_zheng.equals(s_fan)){
                    if(s_zheng.length()>result.length()){
                        result = s_zheng;
                    }
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Solution1 solution = new Solution1();
        System.out.println(solution.longestPalindrome("aaab"));

    }
}
