package com.zifang.util.monitor.jvm.ref;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

/**
 * SoftReference： SoftReference 在“弱引用”中属于最强的引用。SoftReference
 * 所指向的对象，当没有强引用指向它时，会在内存中停留一段的时间，垃圾回收器会根据 JVM 内存的使用情况（内存的紧缺程度）以及 SoftReference
 * 的 get() 方法的调用情况来决定是否对其进行回收。 具体使用一般是通过 SoftReference
 * 的构造方法，将需要用弱引用来指向的对象包装起来。当需要使用的时候，调用 SoftReference 的 get() 方法来获取。当对象未被回收时
 * SoftReference 的 get() 方法会返回该对象的强引用。如下：
 */
public class SoftReferenceDemo {

    static class Bean {
        private String name;
        private int age;

        public Bean(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

    }

    public static void test1() {
        SoftReference<Bean> softBean = new SoftReference<Bean>(new Bean("name", 10));
        Bean bean = softBean.get();
        System.out.println(bean.getName());// “name:10”

        System.gc();
        System.runFinalization();
        // 软引用所指向的对象会根据内存使用情况来决定是否回收，这里内存还充足，所以不会被回收。
        System.out.println(bean.getName());// “name:10”
    }

    /**
     * JVM参数：-Xmx2m - Xms2m
     * 总结：在新开辟 100000 个 Bean对象时，由于软引用会视内存使用情况来判断是否自动回收，所以当最大 Heap 阈值达到 2m 时，系统自动回收最前面开辟的对象，取第 100 个对象时，返回为 null。
     */
    public static void test2() {
        Reference<Bean>[] referent = new SoftReference[100000];
        for (int i = 0; i < referent.length; i++) {
            referent[i] = new SoftReference<Bean>(new Bean("mybean:" + i, 100));
        }
        System.out.println(referent[100].get());// “null”
    }

    public static void main(String[] args) {
        test1();
        test2();
    }

}
