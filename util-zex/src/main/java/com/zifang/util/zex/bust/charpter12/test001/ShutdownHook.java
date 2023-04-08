package com.zifang.util.zex.bust.charpter12.test001;

public class ShutdownHook {
    public static void main(String[] args) {
        System.out.println("JVM-start");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("jvm-关闭关闭关闭");
        }));
    }
}
