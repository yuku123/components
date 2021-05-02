package com.zifang.util.core;

import java.io.IOException;

public class Helllo4 {

    public static final String aaaaa = "干饭人万岁！";

    public static int foo(int i, int j) throws IOException {
        int c = i + j + 1;
        return c;
    }

    public static void testException(String a, Integer b){
        try {
            foo(1,2);
        } catch (IOException e){
            e.printStackTrace();
        } catch (NullPointerException exception){
            System.out.println("干饭人万万岁@@@@!!!!!");
        }
    }

    public static void main(String[] args) {
        testException("吃饭",100);
    }
}
