package com.zifang.demo.jdk.java.net.bookdemo;

import java.net.*;
import java.io.*;

public class LowPortScanner {

	public static void main(String[] args) {

		String host = args.length > 0 ? args[0] : "localhost";

		for (int i = 1; i < 1024; i++) {
			try {
				Socket s = new Socket(host, i);
				System.out.println("There is a server on port " + i + " of " + host);
				s.close();
			} catch (UnknownHostException ex) {
				System.err.println(ex);
				break;
			} catch (IOException ex) {
				// must not be a server on this port
			}
		}
	}
}