package com.zifang.util.proxy.ct;

import javassist.*;

public class Main {
    public static void main(String[] args) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.getCtClass("com.zifang.util.proxy.ct.Main");
        CtConstructor classInitial = ctClass.getClassInitializer(); // 构造函数

        for (CtMethod method : ctClass.getDeclaredMethods()) { // 这个类下的所有的方法

        }

        for (CtField declaredField : ctClass.getDeclaredFields()) {


        }
    }

}