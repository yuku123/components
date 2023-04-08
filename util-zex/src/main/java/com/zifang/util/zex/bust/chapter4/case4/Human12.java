package com.zifang.util.zex.bust.chapter4.case4;

class Human12{
    static{
        System.out.println("Human12.static{}");
    }

    {
        System.out.println("Human12.{}");
    }

    private static Human12Field1 human12Field1 = new Human12Field1();
    private Human12Field2 human12Field2 = new Human12Field2();

    public Human12(){
        System.out.println("Human12.()");
    }

    private String age;
    public void eat(){
        System.out.println("我要吃饭");
        goWc();
    }
    private void goWc(){
        System.out.println("吃完饭就上个厕所");
    }
}

class Coder2 extends Human12{

    static{
        System.out.println("Coder2.static{}");
    }

    {
        System.out.println("Coder2.{}");
    }

    private static Coder2Field1 coder2Field1 = new Coder2Field1();
    private Coder2Field2 coder2Field2 = new Coder2Field2();


    public Coder2(){
        System.out.println("Coder2.()");
    }

    public void wirteCode(){
        System.out.println("愉快地写代码");
    }
    public static void main(String[] args){
        Coder2 coder = new Coder2();
        coder.eat();// human的行为
        coder.wirteCode();// 张三的行为
    }
}

class Human12Field1{
    public Human12Field1(){
        System.out.println("Human12Field1.()");
    }
}
class Human12Field2{
    public Human12Field2(){
        System.out.println("Human12Field2.()");
    }
}


class Coder2Field1{
    public Coder2Field1(){
        System.out.println("Coder2Field1.()");
    }
}
class Coder2Field2{
    public Coder2Field2(){
        System.out.println("Coder2Field2.()");
    }
}