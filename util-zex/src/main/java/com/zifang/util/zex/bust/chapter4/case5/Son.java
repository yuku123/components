package com.zifang.util.zex.bust.chapter4.case5;


class Field {

    static{
        System.out.println("Field-initial-static");
    }

    {
        System.out.println("Field-not-initial-static");
    }

    public Field(String str){
        System.out.println(str+"-Field-construct");
    }
}
class Father{

    private Field field = new Field("father");

    static{
        System.out.println("father-initial-static");
    }

    {
        System.out.println("father-not-initial-static");
    }

    public Father(){
        System.out.println("father-construct");
    }

}

public class Son extends Father{

    private Field field = new Field("son");

    static{
        System.out.println("Son-initial-static");
    }

    {
        System.out.println("Son-not-initial-static");
    }

    public Son(){
        System.out.println("Son-construct");
    }

    public static void main(String[] args) {
        Son son = new Son();
    }
}
