package com.zifang.util.zex.bytecode.ctclass;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.net.MalformedURLException;

public class CtClassTest {

    public static void main(String[] args) throws MalformedURLException, NotFoundException {

        ClassPool pool = new ClassPool(ClassPool.getDefault());
        pool.appendClassPath("/Users/zifang/Downloads/trash/bpmn-engine-1.2.1-SNAPSHOT.jar");
        CtClass ctClass = pool.get("com.come2future.bpmn.WorkflowContext");
        System.out.println(ctClass);

        ClassPool classPool = ClassPool.getDefault();
        classPool.insertClassPath("/Users/zifang/Downloads/trash");

//        URL u = new File("/Users/zifang/Downloads/bpmn-engine-1.2.1-SNAPSHOT.jar").toURI().toURL();
//        URL[] US= new URL[]{u};
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        CustomerUrlClassLoader c = new CustomerUrlClassLoader(US,classLoader);
//        //c.classes;
        System.out.println();

    }

}
