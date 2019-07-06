package com.zifang.demo.jdk.java.net.bookdemo;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

// Requires Java 7 for try-with-resources and multi-catch
public class PooledWeblog {

	private final static int NUM_THREADS = 4;

	public static void main(String[] args) throws IOException {
		ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
		Queue<LogEntry> results = new LinkedList<LogEntry>();

		try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), "UTF-8"));) {
			for (String entry = in.readLine(); entry != null; entry = in.readLine()) {
				LookupTask task = new LookupTask(entry);
				Future<String> future = executor.submit(task);
				LogEntry result = new LogEntry(entry, future);
				results.add(result);
			}
		}

		// Start printing the results. This blocks each time a result isn't
		// ready.
		for (LogEntry result : results) {
			try {
				System.out.println(result.future.get());
			} catch (InterruptedException | ExecutionException ex) {
				System.out.println(result.original);
			}
		}

		executor.shutdown();
	}

	private static class LogEntry {
		String original;
		Future<String> future;

		LogEntry(String original, Future<String> future) {
			this.original = original;
			this.future = future;
		}
	}
}