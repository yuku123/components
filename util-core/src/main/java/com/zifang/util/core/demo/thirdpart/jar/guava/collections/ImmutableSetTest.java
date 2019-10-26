//package com.zifang.util.core.demo.thirdpart.jar.guava.collections;
//
//import com.google.common.collect.ImmutableSet;
//
//import java.awt.*;
//import java.util.HashSet;
//
///**
// * 不可变类集合
// * */
//public class ImmutableSetTest {
//
//    /**
//     * 三种不同的初始化方式，得到不可变类的集合
//     * */
//    public static void test1(){
//        //初始化一般的集合
//        HashSet hashSet = new HashSet();
//        hashSet.add("a");
//        hashSet.add("b");
//        //第一种方式初始化
//        ImmutableSet immutableSet1 = ImmutableSet.copyOf(hashSet);
//        //第二种方式初始化
//        ImmutableSet immutableSet2 = ImmutableSet.of("a", "b", "c");
//        //第三种方式初始化(build的方式)
//        ImmutableSet<Color> mmutableSet3 =
//        ImmutableSet.<Color>builder()
//                .add(new Color(0, 191, 255))
//                .build();
//
//        System.out.print(mmutableSet3);
//
//    }
//
//    public static void main(String[] args) {
//
//        test1();
//
//    }
//}
