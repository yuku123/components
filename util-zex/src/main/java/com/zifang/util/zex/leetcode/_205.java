package com.zifang.util.zex.leetcode;

import java.util.Arrays;
import java.util.HashSet;

public class _205 {
    public boolean isIsomorphic(String s, String t) {

        HashSet<Character> h1 = new HashSet<>();
        for(char c1 : s.toCharArray()){
            h1.add(c1);
        }

        HashSet<Character> h2 = new HashSet<>();
        for(char c2 : t.toCharArray()){
            h2.add(c2);
        }

        return h1.size() == h2.size();
    }

    public static void main(String[] args) {
        new _205().isIsomorphic("foo","bar");
    }
}
