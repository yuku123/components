package com.zifang.demo.jdk.java.net.bookdemo;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.concurrent.*;
import java.util.logging.*;

public class LoggingDaytimeServer {

	public final static int PORT = 13;
	private final static Logger auditLogger = Logger.getLogger("requests");
	private final static Logger errorLogger = Logger.getLogger("errors");

	public static void main(String[] args) {

		ExecutorService pool = Executors.newFixedThreadPool(50);

		try (ServerSocket server = new ServerSocket(PORT)) {
			while (true) {
				try {
					Socket connection = server.accept();
					Callable<Void> task = new DaytimeTask(connection);
					pool.submit(task);
				} catch (IOException ex) {
					errorLogger.log(Level.SEVERE, "accept error", ex);
				} catch (RuntimeException ex) {
					errorLogger.log(Level.SEVERE, "unexpected error " + ex.getMessage(), ex);
				}
			}
		} catch (IOException ex) {
			errorLogger.log(Level.SEVERE, "Couldn't start server", ex);
		} catch (RuntimeException ex) {
			errorLogger.log(Level.SEVERE, "Couldn't start server: " + ex.getMessage(), ex);
		}
	}

	private static class DaytimeTask implements Callable<Void> {

		private Socket connection;

		DaytimeTask(Socket connection) {
			this.connection = connection;
		}

		@Override
		public Void call() {
			try {
				Date now = new Date();
				// write the log entry first in case the client disconnects
				auditLogger.info(now + " " + connection.getRemoteSocketAddress());
				Writer out = new OutputStreamWriter(connection.getOutputStream());
				out.write(now.toString() + "\r\n");
				out.flush();
			} catch (IOException ex) {
				// client disconnected; ignore;
			} finally {
				try {
					connection.close();
				} catch (IOException ex) {
					// ignore;
				}
			}
			return null;
		}
	}
}