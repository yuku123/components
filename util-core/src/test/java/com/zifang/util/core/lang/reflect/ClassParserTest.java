package com.zifang.util.core.lang.reflect;

import org.junit.Test;

public class ClassParserTest {

    @Test
    public void test1(){
        ClassParser classParser = new ClassParser(A.class);
        assert classParser.getCurrentPublicField().size() == 1;
        assert classParser.getCurrentPrivateField().size() == 1;
        assert classParser.getCurrentProtectedField().size() == 1;
        assert classParser.getCurrentAllField().size() == 3;

        assert classParser.getCurrentAllMethod().size() == 4;
        assert classParser.getCurrentPrivateMethod().size() == 1;
        assert classParser.getCurrentProtectedMethod().size() == 1;
        assert classParser.getCurrentPublicMethod().size() == 1;
        assert classParser.getCurrentDefaultMethod().size() == 1;
    }

    @Test
    public void test2(){

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