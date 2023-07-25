package com.zifang.util.zex.bust.chapter5;

public class Exception004 {

    public static void main(String[] args) {
        int re = bar();
        System.out.println(re);
    }

    private static int bar() {
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            throw new Exception("我将被忽略，因为下面的finally中使用了return");
        } finally {
            return 2;
        }
    }
}
