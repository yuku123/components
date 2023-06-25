package com.zifang.util.zex.bust.chapter4.case4;

class Test {
    static {
        System.out.println("Test.static{}");
    }

    {
        System.out.println("Test.{}");
    }

    private static Test1 staticTest1 = new Test1("staticTest1");
    private Test1 test1 = new Test1("Test1");

    public Test() {
        System.out.println("Test()");
    }

    public static void main(String[] args) {
        Test test = new Test();
    }
}

class Test1 {
    public Test1(String str) {
        System.out.println("test1(" + str + ")");
    }
}
