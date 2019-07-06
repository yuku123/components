package com.zifang.demo.temp.sun.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * Java堆分析工具（jhat）正如它名字描述的那样：分析dump堆信息。在下面的小例子里，我们构造了一个 OutOfMemoryError ，然后给这个
 * java 进程指定 -XX:+HeapDumpOnOutOfMemoryError ，这样运行时就会产生一个 dump 文件供我们分析。
 * 可以把堆内存设置小一点，容易测试 -Xmx32M
 * 
 * 产生java_pid5644.hprof 文件，然后执行下面命令
 * 	jhat java_pid5644.hprof 
 * 
 * 通过下面地址访问：
 * 	http://localhost:7000
 * 
 * 从Heap Histogram(堆直方图)
 * 现在我们可以清晰地看到拥有 393567 结点的 HashMap 就是导致程序崩溃的元凶。虽然有更多可以检查内存分布使用情况和堆分析的工具，但是jhat是内置的，是分析的一个好的开端。
 * @author jijs
 *
 */
public class Jhat {

	private static Map map = new HashMap();

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				System.out.println("We have accumulated " + map.size() + "entries");
			}

		});
		for (int i = 0;; i++) {
			map.put(Integer.toBinaryString(i), i);
		}

	}
}
