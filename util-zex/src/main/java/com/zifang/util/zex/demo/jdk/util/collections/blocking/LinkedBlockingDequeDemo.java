package com.zifang.util.zex.demo.jdk.util.collections.blocking;

import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * 使用阻塞式线程安全列表

	最基本的集合类型是列表。一个列表包含的元素数量不定，可以在任何位置添加、读取或移除元素。并发列表允许不同的线程在同一时间添加或移除列表中的元素，而不会造成数据不一致。
	在本节，你会学到如何在并发程序中使用阻塞式列表。阻塞式列表与非阻塞式列表的主要差别是：阻塞式列表在插入和删除操作时，如果列表已满或为空，操作不会被立即执行，而是将调用这个操作的线程阻塞队列直到操作可以执行成功。Java引入了LinkedBlocking Deque类来实现阻塞式列表。
	将要实现的范例包括以下两个不同的任务：
		1.添加大量的数据到一个列表中；
		2.从同一个列表中移除大量的数据。
 *
 *
 */
public class LinkedBlockingDequeDemo {

	// 1．创建名为Client的类，并实现Runnable接口。
	static class Client implements Runnable {
		// 2．声明一个私有的LinkedBlockingDeque属性requestList，并指定它的泛型参数是String型的。
		private LinkedBlockingDeque<String> requestList;

		// 3．实现类的构造器来初始化属性。
		public Client(LinkedBlockingDeque<String> requestList) {
			this.requestList = requestList;
		}

		// 4．实现run()方法。使用requestList对象的put()方法，每两秒向列表requestList中插入5个字符串。重复3次。
		@Override
		public void run() {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 5; j++) {
					StringBuilder request = new StringBuilder();
					request.append(i);
					request.append(":");
					request.append(j);
					try {
						requestList.put(request.toString());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.printf("Client: %s at %s.\n", request, new Date());
				}
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.printf("Client: End.\n");
		}
	}

	// 5． 创建范例的主类Main，并添加main()方法。
	public static void main(String[] args) throws Exception {
		// 6． 声明并创建LinkedBlockingDeque属性list，并指定它的泛型参数是String型的。
		LinkedBlockingDeque<String> list = new LinkedBlockingDeque<>(3);
		// 7．将client作为传入参数创建线程Thread并启动。
		Client client = new Client(list);
		Thread thread = new Thread(client);
		thread.start();
		// 8．使用list对象的take()方法，每300毫秒从列表中取出3个字符串对象，重复5次。在控制台输出字符串。
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 3; j++) {
				String request = list.take();
				System.out.printf("Main: Request: %s at %s. Size:%d\n", request, new Date(), list.size());
			}
			TimeUnit.MILLISECONDS.sleep(300);
		}
		// 9．输出一条表示程序结束的消息。
		System.out.printf("Main: End of the program.\n");
	}

}

/**
 * 工作原理

	本节使用的泛型参数是String的LinkedBlockingDeque对象，用来实现一个阻塞式并发数据列表。
	Client类使用put()方法将字符串插入到列表中。如果列表已满（列表生成时指定了固定的容量），调用这个方法的线程将被阻塞直到列表中有了可用的空间。
	Main类使用take()方法从列表中取字符串。如果列表为空，调用这个方法的线程将被阻塞直到列表不为空（即有可用的元素）。
	这个例子中使用了LinkedBlockingDeque对象的两个方法，调用它们的线程可能会被阻塞，在阻塞时如果线程被中断，方法会抛出InterruptedException异常，所以必须捕获和处理这个异常。
	更多信息

	LinkedBlockingDeque类也提供了其他存取元素的方法，这些方法不会引起阻塞，而是抛出异常或返回null。
		1.takeFirst()和takeLast()：分别返回列表中第一个和最后一个元素，返回的元素会从列表中移除。如果列表为空，调用方法的线程将被阻塞直到列表中有可用的元素出现。
		2.getFirst()和getLast()：分别返回列表中第一个和最后一个元素，返回的元素不会从列表中移除。如果列表为空，则抛出NoSuchElementExcpetinon异常。
		3.peek()、peekFirst()和peekLast()：分别返回列表中第一个和最后一个元素，返回的元素不会从列表中移除。如果列表为空，返回null。
		4.poll()、pollFirst()和pollLast()：分别返回列表中第一个和最后一个元素，返回的元素将会从列表中移除。如果列表为空，返回null。
		5.add()、addFirst()和addLast(): 分别将元素添加到列表中第一位和最后一位。如果列表已满（列表生成时指定了固定的容量），这些方法将抛出IllegalStateException异常。
*/