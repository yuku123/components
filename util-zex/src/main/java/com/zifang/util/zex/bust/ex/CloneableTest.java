package com.zifang.util.zex.bust.ex;

class CloneField implements Cloneable {

    private Long l = System.currentTimeMillis();

}
public class CloneableTest implements Cloneable {

    public String name;

    public CloneField field = new CloneField();

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        CloneableTest cloneableTest = new CloneableTest();
        cloneableTest.name = "吃饭";

        CloneableTest cloneableTest1 = (CloneableTest) cloneableTest.clone();
        System.out.println(cloneableTest1.name);
        System.out.println(cloneableTest.field == cloneableTest1.field);

    }
}

