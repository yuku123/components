package com.zifang.util.zex.bust.chapter5;

public class Exception005 {

    public static void main(String[] args) {
        int re = bar();
        System.out.println(re);
    }

    private static int bar() {
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            throw new RuntimeException("我将被忽略，因为下面的finally抛出了新异常");
        } finally {
            throw new RuntimeException("finally的错误");
        }
    }
}
