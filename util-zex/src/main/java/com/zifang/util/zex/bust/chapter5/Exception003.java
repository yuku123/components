package com.zifang.util.zex.bust.chapter5;

public class Exception003 {

    public static void main(String[] args){
        int re = bar();
        System.out.println(re);
    }
    private static int bar() {
        try{
            int i = 1/0;
        } catch (Exception e){
            return 1;
        }
        finally{
            return 2;
        }
    }
}
