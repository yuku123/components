package com.zifang.util.zex.bust.chapter4.case2;

class Human {
    String sex;
    String age;

    Human(String sex, String age) {
        sex = sex;
        age = age;
        // 从外部接收到信息，至于如何处理这部分信息全部由类自己来控制
    }

    void eat() {
        System.out.println("吃饭");
    }

    void sleep() {
        System.out.println("睡觉");
    }
}

class Main {
    public static void main(String[] args) {
        Human human = new Human("男", "26岁");
        human.eat();
        human.sleep();
    }
}