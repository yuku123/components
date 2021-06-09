package com.zifang.util.core.bytecode;

import java.util.ArrayList;
import java.util.List;

public class TestByteCode {

    private int int_2_2_1 = 1;

    public void test2_1_1_1(int i, int j) {
        int k = int_2_2_1 + i + j;
        System.out.println(k);
    }

    public void test2_1_1_2() {
        int i = 1;
        int j = 2;
        int k = int_2_2_1 + i + j;
        System.out.println(k);
    }

    public int test2_1_2_1() {
        int i = 1;
        int j = 2;
        int k = int_2_2_1 + i + j;
        return k;
    }

    public int test2_3_1(int n) {
        if (n > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public static void test_2_2_1() {
        int i = 0;
        for (int j = 0; j < 50; j++) {
            i = i++;
        }
        System.out.println(i);
    }

    public static void test_2_2_2() {
        int i = 0;
        for (int j = 0; j < 50; j++) {
            i = ++i;
        }
        System.out.println(i);
    }

    public void test_2_3_2(int[] c) {
        for (int i = 0; i < c.length; i++) {
            System.out.println(i);
        }
    }

    public void test_2_3_2_1_1() {
        int[] numbers = new int[]{1, 2, 3};
        for (int number : numbers) {
            System.out.println(number);
        }
    }

    public void test_2_3_2_1_2() {
        List<String> a = new ArrayList<>();
        a.add("a");
        a.add("b");
        a.add("c");
        for (String item : a) {
            System.out.println(item);
        }
    }

    public int test_2_3_3_1_1(int i) {
        switch (i) {
            case 100:
                return 0;
            case 101:
                return 1;
            case 104:
                return 4;
            default:
                return -1;
        }
    }

    public int test_2_3_3_1_2(int i) {
        switch (i) {
            case 1:
                return 0;
            case 10:
                return 1;
            case 100:
                return 4;
            default:
                return -1;
        }
    }

    public int test_2_3_3_2(String name) {
        switch (name) {
            case "吃饭1":
                return 100;
            case "吃饭2":
                return 200;
            default:
                return -1;
        }
    }


    public void test2_5_1_exception() {
        throw new RuntimeException();
    }

    public void test2_5_1_handler(Exception e) {
        System.out.println("捕获到异常");
    }

    public void test2_5_finally() {
        System.out.println("finally语句块");
    }

    public void test2_5_1() {
        try {
            test2_5_1_exception();
        } catch (RuntimeException e) {
            test2_5_1_handler(e);
        }
    }

    public void test2_5_2() {
        try {
            test2_5_1_exception();
        } catch (NullPointerException e) {
            test2_5_1_handler(e);
        } catch (RuntimeException e) {
            test2_5_1_handler(e);
        }
    }

    public void test2_5_3() {
        try {
            test2_5_1_exception();
        } catch (NullPointerException e) {
            test2_5_1_handler(e);
        } finally {
            test2_5_finally();
        }
    }

    public int test2_5_3_1() {
        try {
            int a = 1 / 0;
            return 0;
        } catch (Exception e) {
            int b = 1 / 0;
            return 1;
        } finally {
            return 2;
        }
    }


    public int test2_5_3_2() {
        int i = 100;
        try {
            return i;
        } finally {
            ++i;
        }
    }

    public int test2_5_3_3() {
        int i = 100;
        try {
            return i;
        } finally {
            i++;
        }
    }

    public String test2_5_3_4() {
        String s = "hello";
        try {
            return s;
        } finally {
            s = "xyz";
        }
    }

    public void test2_7_1() {
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("hello, inner class");
            }
        };
        r1.run();
    }

    public void test2_7_2() {
        Runnable r1 = () -> System.out.println("hello, inner class");
        r1.run();
    }


    public static void main(String[] args) {
        new TestByteCode().test2_1_2_1();
        test_2_2_2();
        new TestByteCode().test2_5_1();

        int test2_5_3_1 = new TestByteCode().test2_5_3_1();
        int test2_5_3_2 = new TestByteCode().test2_5_3_2();
        int test2_5_3_3 = new TestByteCode().test2_5_3_3();
        String test2_5_3_4 = new TestByteCode().test2_5_3_4();
        System.out.println(test2_5_3_1);
        System.out.println(test2_5_3_2);
        System.out.println(test2_5_3_3);
        System.out.println(test2_5_3_4);


    }
}
