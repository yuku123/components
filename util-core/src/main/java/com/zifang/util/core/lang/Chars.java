package com.zifang.util.core.lang;

/**
 * 针对 char的一些操作方法 合集
 * */
public class Chars {

    /**
     * 表达是否为字母
     * */
    public static boolean isAlpha(char ch) {
        return ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z';
    }

    /**
     * 表达是否为数字
     * */
    public static boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    /**
     * 表达是否为空白字符 -> ' ' , '\t' , '\n' 都算
     * */
    public static boolean isBlank(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\n';
    }
}
