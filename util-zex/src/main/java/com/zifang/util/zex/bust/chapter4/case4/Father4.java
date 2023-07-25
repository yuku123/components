package com.zifang.util.zex.bust.chapter4.case4;

class Father4 {

    protected int money = 100;

    public void fishing() {
        System.out.println("钓鱼");
    }

}

class Son4 extends Father4 {

    private int money = 50;

    public void writeCode() {
        System.out.println("写代码");
    }

    public void getMoney() {
        System.out.println(super.money);
    }


    public static void main(String[] args) {
        Son4 son = new Son4();
        son.fishing();
        son.writeCode();
        son.getMoney();
    }
}

