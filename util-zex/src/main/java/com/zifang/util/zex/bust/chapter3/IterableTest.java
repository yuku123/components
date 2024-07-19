package com.zifang.util.zex.bust.chapter3;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class IterableTest {

    @Test
    public void test001(){
        Collection<String> c = Arrays.asList("1","2","3");
        Iterator<String> iterator =  c.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }

    @Test
    public void test002(){
        Collection<String> c = Arrays.asList("1","2","3");
        for(String s : c){
            System.out.println(s);
        }
    }
}
