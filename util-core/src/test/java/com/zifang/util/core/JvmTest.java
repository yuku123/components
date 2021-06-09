package com.zifang.util.core;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JvmTest {

    @Test
    public void test0(){
        List<HeapOOM> list= new ArrayList<>();
        while(true){
            list.add(new HeapOOM());
        }
    }

    @Test
    public void test1(){
        StackOverflow stackOverflowError = new StackOverflow();
        try {
            stackOverflowError.stackLeak();
        }catch (Throwable e){
            System.out.println("stack 深度:"+stackOverflowError.stackLength);
            throw e;
        }
    }
}

class HeapOOM {
    HeapOOM[] testlist= new HeapOOM[100000000];
}

class StackOverflow {
    int stackLength = 1;
    public void stackLeak() {
        stackLength++;
        stackLeak();
    }
}