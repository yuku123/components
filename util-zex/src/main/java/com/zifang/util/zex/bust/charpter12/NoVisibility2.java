package com.zifang.util.zex.bust.charpter12;

public class NoVisibility2 {

    private static int n1 = 0;

    private static int number = 0;

    private static void test1(){
        new Thread (){
            @Override
            public  void run(){
                number = number +1;
                int a = number;
                while (number - a != 0){
                    System.out.println("不一致");
                }
            }
        }.start();

        while (n1 == 0){
        }
    }

    public static void main(String[] args) {

        test1();

    }

}