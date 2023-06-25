package com.zifang.util.zex.bust.chapter8;

import java.util.ArrayList;
import java.util.List;

public class Handle {
    public static void main(String[] args) {
        List<? extends Number> numberList = new ArrayList<Integer>() {
            {
                add(1);
                add(2);
            }
        };
        Number number = numberList.get(0);
        //Integer i = numberList.get(0);
    }
}