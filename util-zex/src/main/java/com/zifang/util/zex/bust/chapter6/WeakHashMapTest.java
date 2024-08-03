package com.zifang.util.zex.bust.chapter6;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
public class WeakHashMapTest {

     class WeakHashMapTest2{
        private String a;
        public WeakHashMapTest2(String a){
            this.a = a;
        }

    }

    public WeakHashMapTest(){

    }
    @Test
    public void test001() throws InterruptedException {

        WeakHashMap<WeakHashMapTest2,String> w = new WeakHashMap<>();
        w.put(new WeakHashMapTest2("1"),"1");
        w.put(new WeakHashMapTest2("2"),"2");
        w.put(new WeakHashMapTest2("3"),"3");
        byte[] bArray = new byte[1024 * 1024 * 1024];
        byte[] bArra1 = new byte[1024 * 1024 * 1024];
        System.out.println("WeakHashMap 的内容：" + w);

        System.gc();

        Thread.sleep(10000);
        System.out.println("WeakHashMap 的内容：" + w);

        System.gc();
        System.gc();
        System.gc();
        System.gc();
        System.gc();
        byte[] bArra14 = new byte[1024 * 1024 * 400];
        System.gc();



    }
}
