package com.zifang.util.core.demo.jdk.java.lang;

public class RuntimeTest {
    /**
     * 程序退出时执行事件
     */
    public static void shutdownHook(){
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                System.out.println("Program exit...");
            }

        });

        System.out.println("run ... ");
    }

    public static void main(String[] args) {
        shutdownHook();
    }
}
