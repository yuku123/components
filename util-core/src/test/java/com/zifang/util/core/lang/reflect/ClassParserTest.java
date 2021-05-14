package com.zifang.util.core.lang.reflect;

import org.junit.Test;

public class ClassParserTest {

    @Test
    public void test1(){
        ClassParser a = new ClassParserFactory().getInstance(A.class);

        assert a.getCurrentPublicField().size() == 1;
        assert a.getCurrentPrivateField().size() == 1;
        assert a.getCurrentProtectedField().size() == 1;
        assert a.getCurrentAllField().size() == 3;

        assert a.getCurrentAllMethod().size() == 4;
        assert a.getCurrentPrivateMethod().size() == 1;
        assert a.getCurrentProtectedMethod().size() == 1;
        assert a.getCurrentPublicMethod().size() == 1;
        assert a.getCurrentDefaultMethod().size() == 1;

        assert a.isNormalClass();
    }

    @Test
    public void test2(){

        ClassParser a = new ClassParserFactory().getInstance(A.class);

    }
}


class A extends AbstractA implements IA1,IA2{
    private String a1;
    protected String a2;
    public String a3;

    public void t1(){}
    private void t2(){}
    protected void t3(){}
    void t4(){}

}

interface IA1{}

interface IA2 extends IA21{}

interface IA21{}

abstract class AbstractA extends B{}

class B{}