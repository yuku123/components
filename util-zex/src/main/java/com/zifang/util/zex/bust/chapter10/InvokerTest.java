package com.zifang.util.zex.bust.chapter10;

import org.junit.Test;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

class InvokeClass{
    public static String staticValue = "staticValue";
    public String noStaticValue = "noStaticValue";

    public InvokeClass(){
        System.out.println("调用默认构造方法");
    }

    public InvokeClass(String desc){
        System.out.println("调用构造方法："+desc);
    }

    public static void staticHandle(){
        System.out.println("staticHandle");
    }

    public void handle(){
        System.out.println("handle");
    }

    @Override
    public String toString(){
        return String.valueOf(this.hashCode());
    }

}
public class InvokerTest {

    @Test
    public void test001() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        System.out.println("\n执行newInstance--");
        InvokeClass i1 = InvokeClass.class.newInstance();

        System.out.println("\n执行调用有参Constructor");
        InvokeClass i2 = InvokeClass.class.getConstructor(String.class).newInstance("初始化");

        System.out.println("\n执行调用无参Constructor");
        InvokeClass i3 = InvokeClass.class.getConstructor().newInstance();

        System.out.println("\n创建对象数组");
        InvokeClass[] invokeClasses = (InvokeClass[]) Array.newInstance(InvokeClass.class,3);
        invokeClasses[0] = i1;
        invokeClasses[1] = i2;
        invokeClasses[2] = i3;
        System.out.println(Arrays.toString(invokeClasses));

    }

    @Test
    public void test002() throws NoSuchFieldException, IllegalAccessException {

        InvokeClass invokeClass = new InvokeClass();

        System.out.println("\n打印两者数据");
        System.out.println("invokeClass.noStaticValue: "+invokeClass.noStaticValue);
        System.out.println("InvokeClass.staticValue: "+InvokeClass.staticValue);

        System.out.println("\n使用反射的方式获得两者数据");
        System.out.println("reflect:invokeClass.noStaticValue: "+invokeClass.getClass().getDeclaredField("noStaticValue").get(invokeClass));
        System.out.println("reflect:InvokeClass.staticValue: "+InvokeClass.class.getDeclaredField("staticValue").get(null));

        System.out.println("\n修改两者数据");
        Field field1 = invokeClass.getClass().getDeclaredField("noStaticValue");
        field1.setAccessible(true);
        field1.set(invokeClass,"noStaticValue-new");

        Field field2 = InvokeClass.class.getDeclaredField("staticValue");
        field2.setAccessible(true);
        field2.set(invokeClass,"staticValue-new");

        System.out.println("\n打印修改后两者数据");
        System.out.println("invokeClass.noStaticValue: "+invokeClass.noStaticValue);
        System.out.println("InvokeClass.staticValue: "+InvokeClass.staticValue);
    }

    @Test
    public void test003() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        InvokeClass invokeClass = new InvokeClass();
        Method method1 = InvokeClass.class.getMethod("staticHandle");
        method1.setAccessible(true);
        Method method2 = InvokeClass.class.getMethod("handle");
        method2.setAccessible(true);

        method1.invoke(null);
        method2.invoke(invokeClass);
    }
}
