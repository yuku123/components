package com.zifang.util.zex.bust.chapter5;

public class Exception002 {

    public static void main(String[] args){
        int re = bar();
        System.out.println(re);
    }
    private static int bar() {
        try{
            System.out.println("处理---");
            return 5;
        } finally{
            System.out.println("finally");
        }
    }
}
