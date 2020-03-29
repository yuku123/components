package com.zifang.util.zex.demo.jdk.collections.blocking;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * 用优先级对使用阻塞线程安全的列表排序
	一个典型的需求是，当你需要使用一个有序列表的数据结构时，Java提供的PriorityBlockingQueue类就拥有这种功能。
	你想要添加到PriorityBlockingQueue中的所有元素必须实现Comparable接口。这个接口有一个compareTo()方法，它接收同样类型的对象，你有两个比较的对象：一个是执行这个方法的对象，另一个是作为参数接收的对象。如果本地对象小于参数，则该方法返回小于0的数值。如果本地对象大于参数，则该方法返回大于0的数值。如果本地对象等于参数，则该方法返回等于0的数值。
	PriorityBlockingQueue使用compareTo()方法决定插入元素的位置。（校注：默认情况下）较大的元素将被放在队列的尾部。
	阻塞数据结构（blocking data structure）是PriorityBlockingQueue的另一个重要特性。它有这样的方法，如果它们不能立即进行它们的操作，则阻塞这个线程直到它们的操作可以进行。
	在这个指南中，你将学习如何使用PriorityBlockingQueue类实现一个例子，你将在相同的列表上使用不同的优先级存储大量事件（event），然后检查队列的排序是否是你想要的。
 *
 *
 */
public class PriorityBlockingQueueDemo {
	// 1.实现Event类，并指定它实现参数化为Event类的Comparable接口。
	static class Event implements Comparable<Event> {
		/// 2.声明一个私有的、int类型的属性thread，用来存储已创建事件的线程数。
		private int thread;
		// 3.声明一个私有的、int类型的属性priority，用来存储事件的优先级。
		private int priority;

		// 4.实现这个类的构造器，并初始化它的属性。
		public Event(int thread, int priority) {
			this.thread = thread;
			this.priority = priority;
		}

		// 5.实现getThread()方法，用来返回thread属性的值。
		public int getThread() {
			return thread;
		}

		// 6.实现getPriority()方法，用来返回priority属性的值。
		public int getPriority() {
			return priority;
		}

		// 7.实现compareTo()方法。它接收Event作为参数，并且比较当前事件与参数的优先级。如果当前事件的优先级更大，则返回-1，如果这两个优先级相等，则返回0，如果当前事件的优先级更小，则返回1。注意，这与大多数Comparator.compareTo()的实现是相反的。
		@Override
		public int compareTo(Event e) {
			if (this.priority > e.getPriority()) {
				return -1;
			} else if (this.priority < e.getPriority()) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	// 8.创建一个Task类，并指定它实现Runnable接口。
	static class Task implements Runnable {
		/// 9.声明一个私有的、int类型的属性id，用来存储任务的数字标识。
		private int id;
		// 10.声明一个私有的、参数化为Event类的PriorityBlockingQueue类型的属性queue，用来存储任务产生的事件。
		private PriorityBlockingQueue<Event> queue;

		// 11.实现这个类的构造器，并初始化它的属性。
		public Task(int id, PriorityBlockingQueue<Event> queue) {
			this.id = id;
			this.queue = queue;
		}

		// 12.实现run()方法。它存储100个事件到队列，使用它们的ID来标识创建事件的任务，并给予不断增加的数作为优先级。使用add()方法添加事件到队列中。
		@Override
		public void run() {
			for (int i = 0; i < 1000; i++) {
				Event event = new Event(id, i);
				queue.add(event);
			}
		}
	}

	// 13.实现这个例子的主类，通过创建Main类，并实现main(）方法。
	public static void main(String[] args) {
		/// 14.创建一个参数化为Event类的PriorityBlockingQueue对象。
		PriorityBlockingQueue<Event> queue = new PriorityBlockingQueue<>();
		// 15.创建一个有5个Thread对象的数组，用来存储执行5个任务的线程。
        Thread[] taskThreads = new Thread[5];
		// 16.创建5个Task对象。存储前面创建的线程数组。
		for (int i = 0; i < taskThreads.length; i++) {
			Task task = new Task(i, queue);

			taskThreads[i] = new Thread(task);
		}
		// 17.启动前面创建的5个线程。
		for (int i = 0; i < taskThreads.length; i++) {
			taskThreads[i].start();
		}
		// 18.使用join()方法，等待这5个线程的结束。
		for (int i = 0; i < taskThreads.length; i++) {
			try {
				taskThreads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// 19.将列队真实大小和存储在它里面的事件写入到控制台。使用poll()方法从队列中取出事件。
		System.out.printf("Main: Queue Size: %d\n", queue.size());
		for (int i = 0; i < taskThreads.length * 1000; i++) {
			Event event = queue.poll();
			System.out.printf("Thread %s: Priority %d\n", event.getThread(), event.getPriority());
		}
		// 20.将队列最后的大小写入到控制台。
		System.out.printf("Main: Queue Size: %d\n", queue.size());
		System.out.printf("Main: End of the program\n");
	}
}
/**
它是如何工作的…
在这个指南中，你已使用PriorityBlockingQueue实现Event对象的一个优先级队列。正如我们在引言中提到的，所有存储在PriorityBlockingQueue的元素必须实现Comparable接口，所以，你已在Event类中实现compareTo()方法。
所有事件都有一个优先级属性。拥有更高优先级的元素将成为队列的第一个元素。当你已实现compareTo()方法，如果执行这个方法的事件拥有比作为参数传入的事件更高的优先级时，它将返回-1。在其他情况下，如果执行这个方法的事件拥有比作为参数传入的事件更低的优先级时，它将返回1。如果这两个对象拥有相同优先级，compareTo()方法将返回0。在这种情况下，PriorityBlockingQueue类并不能保证元素的顺序。
我们已实现Task类来添加Event对象到优先级队列中。每个任务对象使用add()方法往队列添加1000个事件（0到99种优先级）。
Main类的main()方法创建5个Task对象，并用相应的线程执行它们。当所有的线程完成它们的执行，你已将所有元素写入到控制台。我们使用poll()方法从队列中获取元素。这个方法返回并删除队列的第一个元素。

你可以看出这个队列如何有5000个元素，第一个元素如何拥有最大的优先级值。
不止这些…
	1.PriorityBlockingQueue类提供其他有趣的方法，以下是其中一些方法的描述：
	2.clear()：这个方法删除队列中的所有元素。
	3.take()：这个方法返回并删除队列中的第一个元素。如果队列是空的，这个方法将阻塞线程直到队列有元素。
	4.put(E e)：E是用来参数化PriorityBlockingQueue类的类。这个方法将作为参数传入的元素插入到队列中。
	5.peek()：这个方法返回列队的第一个元素，但不删除它。

*/