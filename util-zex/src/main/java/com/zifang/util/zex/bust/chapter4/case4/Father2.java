package com.zifang.util.zex.bust.chapter4.case4;

class Father2 {

    protected int money = 100;

    public void fishing() {
        System.out.println("钓鱼");
    }

}

class Son2 extends Father2 {

    public void writeCode() {
        System.out.println("写代码");
    }

    public static void main(String[] args) {
        Son2 son = new Son2();
        son.fishing();
        son.writeCode();
        System.out.println("获得到私房钱：" + son.money);
    }
}

