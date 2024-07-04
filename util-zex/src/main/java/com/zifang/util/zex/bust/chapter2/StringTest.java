package com.zifang.util.zex.bust.chapter2;

import org.junit.Test;

public class StringTest {
    @Test
    public void test001(){
        String a = "123";
        System.out.println(a);
    }

    @Test
    public void test002(){
        String a = new String("123");
        System.out.println(a);
    }

    @Test
    public void test003(){
        String a = new String("12😄4");

        // 打印5
        System.out.println(a.length());
        // 打印4
        System.out.println(a.codePoints().count());

    }
    @Test
    public void test004(){

        String a = new String("12😄4");
        a.indexOf("\uD83D\uDE04");
        // 打印5
        System.out.println(a.length());
        // 打印4
        System.out.println(a.codePoints().count());

    }

}
