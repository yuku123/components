package com.zifang.util.zex.bust.chapter4.case4;

class Father1 {

    protected int money = 100;

    public void fishing() {
        System.out.println("钓鱼");
    }

}

class Son1 extends Father1 {

    public void writeCode() {
        System.out.println("写代码");
    }

    public static void main(String[] args) {
        Son1 son = new Son1();
        son.fishing();
        son.writeCode();
    }
}