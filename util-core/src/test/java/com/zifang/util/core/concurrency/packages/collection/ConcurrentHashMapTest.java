package com.zifang.util.core.concurrency.packages.collection;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapTest {
    public static void main(String[] args) {
        ConcurrentHashMap<String,String> stringStringConcurrentHashMap= new ConcurrentHashMap<>();
        int i = 0;
        while(true){

            if(i == 100){
                System.out.println();
            }
            stringStringConcurrentHashMap.put(""+i,""+i);
            i++;


        }
    }
}
