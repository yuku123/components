package com.zifang.util.zex.bust.chapter8;

import java.util.ArrayList;
import java.util.List;

public class Handle2 {
    public static void main(String[] args) {
        List<? super Integer> numberList = new ArrayList<Integer>(){
            {
                add(1);
                add(2);
            }
        };
        numberList.add(1);
        Object o = numberList.get(0);
        //Integer number = numberList.get(0);
    }
}