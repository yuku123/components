package com.zifang.util.core.resource.praser.xml.example.annotations;

public class Mais {
    public static void main(String[] args) {
        Son son = new Son();
        father father = son;
        father.f();
    }
}


class Son extends father{
    public void f(){
        System.out.println("son");
    }
}

class father{
    public void f(){
        System.out.println("father");
    }
}