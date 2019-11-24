package com.zifang.util.zex.demo.jdk.util.concurent.packages.forkjoin;

import static org.junit.Assert.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

import org.junit.Test;

public class MyForkJoinTaskTest {

	/******************** 测试ForkJoinPool.invoke(...)： *********************/
	@Test
	public void testForkJoinInvoke() throws InterruptedException, ExecutionException {
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		MyForkJoinTask<String> task = new MyForkJoinTask<String>();
		task.setSuccess(true);
		task.setRawResult("test");
		String invokeResult = forkJoinPool.invoke(task);
		System.out.println(invokeResult);
		assertEquals(invokeResult, "test");
	}

	@Test
	public void testForkJoinInvoke2() throws InterruptedException, ExecutionException {
		final ForkJoinPool forkJoinPool = new ForkJoinPool();
		final MyForkJoinTask<String> task = new MyForkJoinTask<String>();
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}

				task.complete("test");
			}
		}).start();

		// exec()返回值是false，此处阻塞，直到另一个线程调用了task.complete(...)
		String result = forkJoinPool.invoke(task);
		System.out.println(result);
	}


	/******************** 测试ForkJoinPool.submit(...)： ********************/
	@Test
	public void testForkJoinSubmit() throws InterruptedException, ExecutionException {
		final ForkJoinPool forkJoinPool = new ForkJoinPool();
		final MyForkJoinTask<String> task = new MyForkJoinTask<String>();

		task.setSuccess(true); // 是否在此任务运行完毕后结束阻塞
		ForkJoinTask<String> result = forkJoinPool.submit(task);
		result.get(); // 如果exec()返回值是false，在此处会阻塞，直到调用complete
	}

	@Test
	public void testForkJoinSubmit2() throws InterruptedException, ExecutionException {
		final ForkJoinPool forkJoinPool = new ForkJoinPool();
		final MyForkJoinTask<String> task = new MyForkJoinTask<String>();

		forkJoinPool.submit(task);
		Thread.sleep(1000);
	}

	@Test
	public void testForkJoinSubmit3() throws InterruptedException, ExecutionException {
		final ForkJoinPool forkJoinPool = new ForkJoinPool();
		final MyForkJoinTask<String> task = new MyForkJoinTask<String>();
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}

				task.complete("test");
			}
		}).start();

		ForkJoinTask<String> result = forkJoinPool.submit(task);
		// exec()返回值是false，此处阻塞，直到另一个线程调用了task.complete(...)
		result.get();
		Thread.sleep(1000);
	}

	/******************* 测试ForkJoinPool.execute(...)： ****************/
	@Test
	public void testForkJoinExecute() throws InterruptedException, ExecutionException {
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		MyForkJoinTask<String> task = new MyForkJoinTask<String>();
		forkJoinPool.execute(task); // 异步执行，无视task.exec()返回值。
	}
}
