package com.zifang.demo.jdk.java.util.collections.blocking;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 *  DelayedQueue类是Java API提供的一种有趣的数据结构，并且你可以用在并发应用程序中。
 *  在这个类中，你可以存储带有激活日期的元素。方法返回或抽取队列的元素将忽略未到期的数据元素。它们对这些方法来说是看不见的。
 *  为了获取这种行为，你想要存储到DelayedQueue类中的元素必须实现Delayed接口。
 *  这个接口允许你处理延迟对象，所以你将实现存储在DelayedQueue对象的激活日期，这个激活时期将作为对象的剩余时间，直到激活日期到来。
 *  这个接口强制实现以下两种方法：
 *    compareTo(Delayed o)：Delayed接口继承Comparable接口。如果执行这个方法的对象的延期小于作为参数传入的对象时，该方法返回一个小于0的值。
 *    	如果执行这个方法的对象的延期大于作为参数传入的对象时，该方法返回一个大于0的值。如果这两个对象有相同的延期，该方法返回0。
 *    getDelay(TimeUnit unit)：该方法返回与此对象相关的剩余延迟时间，以给定的时间单位表示。
 *    	TimeUnit类是一个枚举类，有以下常量：DAYS、HOURS、 MICROSECONDS、MILLISECONDS、 MINUTES、 NANOSECONDS 和 SECONDS。
 */
public class DelayQueueDemo {

	/// 1.创建一个实现Delayed接口的Event类。
	static class Event implements Delayed {
		/// 2.声明一个私有的、Date类型的属性startDate。
		private Date startDate;

		/// 3.实现这个类的构造器，并初始化它的属性。
		public Event(Date startDate) {
			this.startDate = startDate;
		}

		/// 4.实现compareTo()方法。它接收一个Delayed对象作为参数。返回当前对象的延期与作为参数传入对象的延期之间的差异。
		@Override
		public int compareTo(Delayed o) {
			long result = this.getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
			if (result < 0) {
				return -1;
			} else if (result > 0) {
				return 1;
			}
			return 0;
		}

		/// 5.实现getDelay()方法。返回对象的startDate与作为参数接收的TimeUnit的真实日期之间的差异。
		public long getDelay(TimeUnit unit) {
			Date now = new Date();
			long diff = startDate.getTime() - now.getTime();
			return unit.convert(diff, TimeUnit.MILLISECONDS);
		}

		/// 6.创建一个实现Runnable接口的Task类。
		static class Task implements Runnable {
			/// 7.声明一个私有的、int类型的属性id，用来存储任务的标识数字。
			private int id;
			/// 8.声明一个私有的、参数化为Event类的DelayQueue类型的属性queue。
			private DelayQueue<Event> queue;

			/// 9.实现这个类的构造器，并初始化它的属性。
			public Task(int id, DelayQueue<Event> queue) {
				this.id = id;
				this.queue = queue;
			}

			/// 10.实现run()方法。首先，计算任务将要创建的事件的激活日期。添加等于对象ID的实际日期秒数。
			@Override
			public void run() {
				Date now = new Date();
				Date delay = new Date();
				delay.setTime(now.getTime() + (id * 1000));
				System.out.printf("Thread %s: %s\n", id, delay);
				/// 11.使用add()方法，在队列中存储100个事件。
				for (int i = 0; i < 100; i++) {
					Event event = new Event(delay);
					queue.add(event);
				}
			}
		}

		/// 12.通过创建Main类，并实现main()方法，来实现这个例子的主类。
		public static void main(String[] args) throws Exception {
			/// 13.创建一个参数化为Event类的DelayedQueue对象。
			DelayQueue<Event> queue = new DelayQueue<>();
			/// 14.创建一个有5个Thread对象的数组，用来存储将要执行的任务。
			Thread threads[] = new Thread[5];
			/// 15.创建5个具有不同IDs的Task对象。
			for (int i = 0; i < threads.length; i++) {
				Task task = new Task(i + 1, queue);
				threads[i] = new Thread(task);
			}
			/// 16.开始执行前面创建的5个任务。
			for (int i = 0; i < threads.length; i++) {
				threads[i].start();
			}
			/// 17.使用join()方法等待任务的结束。
			for (int i = 0; i < threads.length; i++) {
				threads[i].join();
			}
			/// 18.将存储在队列中的事件写入到控制台。当队列的大小大于0时，使用poll()方法获取一个Event类。如果它返回null，令主线程睡眠500毫秒，等待更多事件的激活。
			do {
				int counter = 0;
				Event event;
				do {
					event = queue.poll();
					if (event != null)
						counter++;
				} while (event != null);
				System.out.printf("At %s you have read %d events\n", new Date(), counter);
				TimeUnit.MILLISECONDS.sleep(500);
			} while (queue.size() > 0);
		}
	}
}

/**
	它是如何工作的…
	在这个指南中，我们已实现Event类。这个类只有一个属性（表示事件的激活日期），实现了Delayed接口，所以，你可以在DelayedQueue类中存储Event对象。
	getDelay()方法返回在实际日期和激活日期之间的纳秒数。这两个日期都是Date类的对象。你已使用getTime()方法返回一个被转换成毫秒的日期，你已转换那个值为作为参数接收的TimeUnit。DelayedQueue类使用纳秒工作，但这一点对于你来说是透明的。
	对于compareTo()方法，如果执行这个方法的对象的延期小于作为参数传入的对象的延期，该方法返回小于0的值。如果执行这个方法的对象的延期大于作为参数传入的对象的延期，该方法返回大于0的值。如果这两个对象的延期相等，则返回0。
	你同时实现了Task类。这个类有一个整数属性id。当一个Task对象被执行，它增加一个等于任务ID的秒数作为实际日期，这是被这个任务存储在DelayedQueue类的事件的激活日期。每个Task对象使用add()方法存储100个事件到队列中。
	最后，在Main类的main()方法中，你已创建5个Task对象，并用相应的线程来执行它们。当这些线程完成它们的执行，你已使用poll()方法将所有元素写入到控制台。这个方法检索并删除队列的第一个元素。如果队列中没有任务到期的元素，这个方法返回null值。你调用poll()方法，并且如果它返回一个Evnet类，你增加计数器。当poll()方法返回null值时，你写入计数器的值到控制台，并且令线程睡眠半秒等待更多的激活事件。当你获取存储在队列中的500个事件，这个程序执行结束。
	以下截图显示程序执行的部分输出：
	3
	你可以看出这个程序当它被激活时，只获取100个事件。
	注意：你必须十分小心size()方法。它返回列表中的所有元素数量，包含激活与未激活元素。
	不止这些…
	DelayQueue类提供其他有趣方法，如下：
		1.clear()：这个方法删除队列中的所有元素。
		2.offer(E e)：E是代表用来参数化DelayQueue类的类。这个方法插入作为参数传入的元素到队列中。
		3.peek()：这个方法检索，但不删除队列的第一个元素。
		4.take()：这具方法检索并删除队列的第一个元素。如果队列中没有任何激活的元素，执行这个方法的线程将被阻塞，直到队列有一些激活的元素。
*/