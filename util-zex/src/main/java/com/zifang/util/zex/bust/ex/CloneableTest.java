package com.zifang.util.zex.bust.ex;

public class CloneableTest implements Cloneable{
    public String name;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        CloneableTest cloneableTest = new CloneableTest();
        cloneableTest.name = "吃饭";

        CloneableTest cloneableTest1 = (CloneableTest)cloneableTest.clone();
        System.out.println(cloneableTest1.name);
    }
}

