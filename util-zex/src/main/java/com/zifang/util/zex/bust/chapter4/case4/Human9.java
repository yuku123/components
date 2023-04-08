package com.zifang.util.zex.bust.chapter4.case4;

class Human9{

    public final String sex = "女的";

    private String age;

    public void eat(){
        System.out.println("我要吃饭");
        goWc();
    }
    private void goWc(){
        System.out.println("吃完饭就上个厕所");
    }
    public static void main(String[] args){
        Human9 human = new Human9();
        human.eat();
        System.out.println(human.sex);
        //human.sex = "男的";
    }
}