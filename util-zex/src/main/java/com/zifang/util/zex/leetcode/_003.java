package com.zifang.util.zex.leetcode;

import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;


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

    @CallerSensitive
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {

        Class clazz = Reflection.getCallerClass(1);
        Object o = clazz.newInstance();

        new _003().lengthOfLongestSubstring("abada");

//        Character
    }
}
