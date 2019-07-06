package com.zifang.demo.jdk.java.net.bookdemo;

import java.io.*;
import java.net.*;

public class LocalPortScanner {

	public static void main(String[] args) {

		for (int port = 1; port <= 65535; port++) {
			try {
				// the next line will fail and drop into the catch block if
				// there is already a server running on the port
				ServerSocket server = new ServerSocket(port);
			} catch (IOException ex) {
				System.out.println("There is a server on port " + port + ".");
			}
		}
	}
}