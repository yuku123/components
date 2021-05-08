package com.zifang.util.monitor.jvm.ref;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;


/**
 * PhantomReference： PhantomReference 是所有“弱引用”中最弱的引用类型。不同于软引用和弱引用，虚引用无法通过 get()
 * 方法来取得目标对象的强引用从而使用目标对象，观察源码可以发现 get() 被重写为永远返回 null。 那虚引用到底有什么作用？其实虚引用主要被用来
 * 跟踪对象被垃圾回收的状态，通过查看引用队列中是否包含对象所对应的虚引用来判断它是否
 * 即将被垃圾回收，从而采取行动。它并不被期待用来取得目标对象的引用，而目标对象被回收前，它的引用会被放入一个 ReferenceQueue
 * 对象中，从而达到跟踪对象垃圾回收的作用。 所以具体用法和之前两个有所不同，它必须传入一个 ReferenceQueue
 * 对象。当虚引用所引用对象被垃圾回收后，虚引用会被添加到这个队列中。如：
 */
public class PhantomReferenceDemo {

    /**
     * 值得注意的是，对于引用回收方面，虚引用类似强引用不会自动根据内存情况自动对目标对象回收，Client 需要自己对其进行处理以防 Heap
     * 内存不足异常。 虚引用有以下特征： 虚引用永远无法使用 get() 方法取得对象的强引用从而访问目标对象。
     * 虚引用所指向的对象在被系统内存回收前，虚引用自身会被放入 ReferenceQueue 对象中从而跟踪对象垃圾回收。
     * 虚引用不会根据内存情况自动回收目标对象。 另外值得注意的是，其实 SoftReference, WeakReference 以及
     * PhantomReference 的构造函数都可以接收一个 ReferenceQueue 对象。当 SoftReference 以及
     * WeakReference 被清空的同时，也就是 Java 垃圾回收器准备对它们所指向的对象进行回收时，调用对象的 finalize()
     * 方法之前，它们自身会被加入到这个 ReferenceQueue 对象中，此时可以通过 ReferenceQueue 的 poll()
     * 方法取到它们。而 PhantomReference 只有当 Java 垃圾回收器对其所指向的对象真正进行回收时，会将其加入到这个
     * ReferenceQueue 对象中，这样就可以追综对象的销毁情况。
     *
     * @throws InterruptedException
     */
    public static void test1() {
        ReferenceQueue<String> refQueue = new ReferenceQueue<String>();
        PhantomReference<String> referent = new PhantomReference<String>("T", refQueue);
        System.out.println(referent.get());// null

        // 只有被垃圾回收后才把referent放进ReferenceQueue中
        System.out.println(refQueue.poll() == referent); // false
        System.gc();
        System.runFinalization();
        System.out.println(refQueue.poll() == referent); // true
    }

    /**
     * JVM参数：-Xmx2m - Xms2m 总结：在新开辟 100000 个 Bean
     * 总结：PhantomReference 类似强引用，它不会自动根据内存情况自动对目标对象回收，所以这里在 Heap 里不断开辟新空间，当达到 2m 阈值时，系统报出 OutOfMemoryError 异常。
     */
    public static void test2() {
//		Reference<Bean>[] referent = new PhantomReference[100000];
//		ReferenceQueue<Bean> queue = new ReferenceQueue<SoftReferenceDemo.Bean>();
//		for (int i = 0; i < referent.length; i++) {
//			referent[i] = new PhantomReference<SoftReferenceDemo.Bean>(new Bean("mybean:" + i, 100), queue);// throw
//																							// Exception
//		}
//
//		System.out.println(referent[100].get());
    }


    public static void main(String[] args) throws InterruptedException {
        test1();
        test2();
    }
}
