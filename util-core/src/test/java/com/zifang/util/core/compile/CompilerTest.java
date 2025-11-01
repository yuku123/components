package com.zifang.util.core.compile;

import java.lang.reflect.InvocationTargetException;

import static com.zifang.util.core.compile.Compiler.compile;

public class CompilerTest {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String s = "package xxxx;\n" +
                "public class A{\n" +
                "    public static void a (){\n" +
                "        System.out.println(\"axx\");\n" +
                "    }\n" +
                "}";
        Class c = compile("xxxx","A",s);
        System.out.println(c.getMethod("a").invoke(null));
    }
}
