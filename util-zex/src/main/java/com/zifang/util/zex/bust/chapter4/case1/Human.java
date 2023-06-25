package com.zifang.util.zex.bust.chapter4.case1;

class Human {
    String sex;
    String age;

    void eat() {
        System.out.println("吃饭");
    }

    void sleep() {
        System.out.println("睡觉");
    }
}

class Main {
    public static void main(String[] args) {
        Human human = new Human();
        human.eat();
        human.sleep();
    }
}
