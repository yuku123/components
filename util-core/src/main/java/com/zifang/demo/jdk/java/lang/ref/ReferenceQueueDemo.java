package com.zifang.demo.jdk.java.lang.ref;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 引用队列（java.lang.ref.ReferenceQueue）　　　　　　　
 * 
 * 引用队列配合Reference的子类等使用，当引用对象所指向的内存空间被GC回收后，该引用对象则被追加到引用队列的末尾
 *
 */
public class ReferenceQueueDemo {

	/**
	 * 此类提供三个方法，没有实现queue接口，也没有实现其它接口类，只提供移除队列的三个方法如下：
	 * 
	 * Reference<? extends T> ReferenceQueue#poll() ，从队列中出队一个元素，若队列为空则返回null。
	 * 
	 * Reference<? extends T> ReferenceQueue#remove()
	 * ，从队列中出队一个元素，若没有则阻塞直到有元素可出队。
	 * 
	 * Reference<? extends T> ReferenceQueue#remove(long timeout)
	 * ，从队列中出队一个元素，若没有则阻塞直到有元素可出队或超过timeout指定的毫秒数（由于采用wait(long
	 * timeout)方式实现等待，因此时间不能保证）。
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ReferenceQueue<Object> weakReferenceReferenceQueue = new ReferenceQueue<>();

	    int M1 = 1024;
	    Object o = new Object();
	    Map<WeakReference<byte[]>, Object> map = new HashMap<>();

	    //创建一个线程监听回收的对象
	    new Thread(new Runnable() {
	        @Override
	        public void run() {
	            try {
	                int cnt = 0;
	                WeakReference<byte[]> k;
	                while ((k = (WeakReference) weakReferenceReferenceQueue.remove()) != null) {
	                    map.remove(k); //在这里我们移除被回收对象的引用
	                    System.out.println((cnt++) + "recycle:" + k);
	                    System.out.println("map.size:" + map.size());
	                }


	            } catch (InterruptedException e) {
	                //结束循环

	            }
	        }
	    }).start();

	    for (int i = 0; i < 10000; i++) {
	        byte[] bytes = new byte[M1];
	        map.put(new WeakReference<>(bytes, weakReferenceReferenceQueue), o);
	    }
	    System.gc();
	    System.out.println("map.size:" + map.size());
	}
}
