package com.zifang.util.zex.source;

import org.junit.Test;

import java.lang.reflect.Method;

public class IntegerTest {
    @Test
    public void test001(){
        System.out.println();
        Integer.parseInt("-2147483649",10);
    }


    @Test
    public void test002(){
        System.out.println();
        Integer.toHexString(17);
    }

    @Test
    public  void test003(){
        System.out.println();
        Integer.toString(65536);

    }

    @Test
    public void test004(){
        System.out.println();
    }

    @Test
    public void ssss(){
        for(Method method: Integer.class.getDeclaredMethods()){
            System.out.println(method.getName() + "("+ method.getParameterTypes()+")");
        }
    }



}
