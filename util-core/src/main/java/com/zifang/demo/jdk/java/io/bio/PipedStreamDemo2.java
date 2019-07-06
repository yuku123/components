package com.zifang.demo.jdk.java.io.bio;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 5.2 在管道中多次写入读取字符串
 * 管道流之所以难用，是因为很多情况下写入管道的数据难以区分“长度”，它的设计理念是“通过管道，将源数据源源不绝的发送到目的地 ”。
 * 因此，如果应用场景为“通过管道,将一段一段的数据一次次的发送到目的地”，就会发现很难使用。
 * 为此，使用Java多线程中的信号量来进行同步可以很好的满足此需求。
 * 源代码二：在线程Sender中反复写入多个字符串，在Reciever中多次接收字符串；使用两个信号量Semaphore来控制写入和读取。
 *
 *
 *
 */
public class PipedStreamDemo2 {
	// 控制读取的信号量，初始状态为0
	public static Semaphore readSignal = new Semaphore(0, true);
	// 控制写入的信号量，初始状态为1，表示允许一次写入
	public static Semaphore writeSignal = new Semaphore(1, true);

	public static void main(String[] args) {
		ExecutorService executorService = Executors.newCachedThreadPool();

		try {
			PipedOutputStream pos = new PipedOutputStream();
			PipedInputStream pis = new PipedInputStream(pos);

			Sender sender = new Sender(pos);
			Reciever reciever = new Reciever(pis);

			executorService.execute(sender);
			executorService.execute(reciever);

		} catch (IOException e) {
			e.printStackTrace();
		}
		executorService.shutdown();
	}

	static class Sender extends Thread {
		private PipedOutputStream pos;

		public Sender(PipedOutputStream pos) {
			super();
			this.pos = pos;
		}

		@Override
		public void run() {
			try {
				for (int i = 0; i < 10; i++) {
					Thread.sleep(new Random().nextInt(1000));
					// 获取写入信号量
					writeSignal.acquire();
					String content = "today is a good day. 今天是个好天气：" + i;
					System.out.println("Sender:" + content);
					pos.write(content.getBytes("utf-8"));
					// 释放读取信号量
					readSignal.release();
				}
				pos.close();
				readSignal.release();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	static class Reciever extends Thread {
		private PipedInputStream pis;

		public Reciever(PipedInputStream pis) {
			super();
			this.pis = pis;
		}

		@Override
		public void run() {
			try {
				byte[] buf = new byte[1024];
				int len = 0;
				String s;
				while (true) {
					// 获取读取信号量
					readSignal.acquire();
					len = pis.read(buf);
					if (len == -1)
						break;
					s = new String(buf, 0, len, "utf-8");
					System.out.println("Reciever:" + s);
					// 释放写入信号量
					writeSignal.release();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}