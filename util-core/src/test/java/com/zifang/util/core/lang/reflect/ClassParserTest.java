package com.zifang.util.core.lang.reflect;

import org.junit.Test;

public class ClassParserTest {
    @Test
    public void classParserTest(){

        ClassParser classParser = new ClassParser(A.class);
        classParser.getCurrentPublicField();



    }
}


class A extends AbstractA implements IA1,IA2{}

interface IA1{}

interface IA2 extends IA21{}

interface IA21{}

abstract class AbstractA extends B{}

class B{}