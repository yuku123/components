package com.zifang.util.zex.bust.chapter4.case4;

class Human8 {
    void handle(int d) {
        System.out.println("handle(int d)");
    }

    void handle(int a, int b) {
        System.out.println("handle(int a ,int b)");
    }

    void handle(int... d) {
        System.out.println("int... d");
    }

    public static void main(String[] args) {
        Human8 human = new Human8();
        human.handle(1);
        human.handle(1, 2);
        human.handle(1, 2, 3);
    }
}
