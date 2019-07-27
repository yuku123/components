package com.zifang.util.core.function.test;

public class Main {
    public static void show(String s,Double a){
        System.out.println(s);
    }

    public void test(MyFun<String> f ){
        f.b("ssss",0.0);
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.test((a,e)->{
            System.out.println(a+e);
        });
    }
}
