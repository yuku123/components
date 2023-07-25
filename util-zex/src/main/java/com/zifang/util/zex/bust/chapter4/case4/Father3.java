package com.zifang.util.zex.bust.chapter4.case4;

class Father3 {

    protected int money = 100;

    public void fishing() {
        System.out.println("钓鱼");
    }

}

class Son3 extends Father3 {

    private int money = 50;

    public void writeCode() {
        System.out.println("写代码");
    }

    public void getMoney() {
        System.out.println(money);
    }


    public static void main(String[] args) {
        Son3 son = new Son3();
        son.fishing();
        son.writeCode();
        son.getMoney();
    }
}

