package com.zifang.util.zex.bust.chapter1.case2;

/* **/
/**
 * 文档注释同样不关心中间是否有换行
 */

/**
 * 但是一般为了美观，文档注释内部换行的话前面会再放个*
 * */
public class HelloWord {

    public static void main(String[] args) {
        // 单行注释
        System.out.println("hello word");

        /* 多行注释，并在一行，看上去就像是单行注释 */
        System.out.println("hello word");

        System.out/*多行注释甚至可以嵌在分隔符前后*/./**/println("hello word");
        System.out.println("hello word");
    }
}