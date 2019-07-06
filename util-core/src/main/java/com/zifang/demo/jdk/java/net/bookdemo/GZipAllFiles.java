package com.zifang.demo.jdk.java.net.bookdemo;

import java.io.*;
import java.util.concurrent.*;

public class GZipAllFiles {

	public final static int THREAD_COUNT = 4;

	public static void main(String[] args) {

		ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT);

		for (String filename : args) {
			File f = new File(filename);
			if (f.exists()) {
				if (f.isDirectory()) {
					File[] files = f.listFiles();
					for (int i = 0; i < files.length; i++) {
						if (!files[i].isDirectory()) { // don't recurse
														// directories
							Runnable task = new GZipRunnable(files[i]);
							pool.submit(task);
						}
					}
				} else {
					Runnable task = new GZipRunnable(f);
					pool.submit(task);
				}
			}
		}

		pool.shutdown();
	}
}