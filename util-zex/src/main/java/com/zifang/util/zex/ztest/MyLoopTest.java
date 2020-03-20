package com.zifang.util.zex.ztest;

public class MyLoopTest {

    public void test2(){
        int[] numbers = new int[]{1, 2, 3};
        for (int number : numbers) {
            System.out.println(number);
        }
    }

    public int chooseNear(int i) {
        switch (i) {
            case 100: return 0;
            case 101: return 1;
            case 104: return 4;
            default: return -1;
        }
    }

    public int chooseNear2(int i) {
        switch (i) {
            case 1: return 0;
            case 100: return 1;
            case 200: return 4;
            default: return -1;
        }
    }

    public static void main(String[] args) {
        new MyLoopTest().test2();
    }

//    public void s(int i,int j){
//        int c = 1;
//        System.out.println(i+j+c);
//    }
}