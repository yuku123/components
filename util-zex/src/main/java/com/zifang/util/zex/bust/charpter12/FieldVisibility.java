package com.zifang.util.zex.bust.charpter12;

public class FieldVisibility {

    int a = 1;
    int b = 2;

    //给a 赋值, 并把值给b
    private void change() {
        a = 3;
        b = a;
    }

    /**
     * 打印出a b
     */
    private void print() {
        System.out.println("b=" + b + ";a=" + a);
    }

    public static void main(String[] args) {

        while (true) {
            FieldVisibility test = new FieldVisibility();
            new Thread(()->{
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //给 a b 重新赋值
                test.change();
            }).start();

            new Thread(()->{
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //给 a b 打印出来
                test.print();
            }).start();
        }
    }
}