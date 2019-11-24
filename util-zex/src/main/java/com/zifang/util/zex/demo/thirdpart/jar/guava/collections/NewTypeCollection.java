package com.zifang.util.zex.demo.thirdpart.jar.guava.collections;//package com.zifang.util.core.demo.thirdpart.jar.guava.collections;
//
//import com.google.common.collect.HashMultiset;
//import com.google.common.collect.Multiset;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 新的集合类型
// * */
//public class NewTypeCollection {
//
//    public static void multisetTest(){
//
//        String strWorld="wer|dfd|dd|dfd|dda|de|dr";
//        String[] words=strWorld.split("|");
//        List<String> wordList=new ArrayList<String>();
//
//        for (String word : words) {
//            wordList.add(word);
//        }
//        Multiset<String> wordsMultiset = HashMultiset.create();
//        wordsMultiset.addAll(wordList);
//
//        for(String key:wordsMultiset.elementSet()){
//            System.out.println(key+" count："+wordsMultiset.count(key));
//        }
//
//    }
//    public static void main(String[] args) {
//
//        multisetTest();
//
//    }
//}