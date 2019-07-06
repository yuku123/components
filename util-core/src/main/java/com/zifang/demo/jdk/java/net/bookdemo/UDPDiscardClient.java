package com.zifang.demo.jdk.java.net.bookdemo;

import java.net.*;
import java.io.*;

public class UDPDiscardClient {

	public final static int PORT = 9;

	public static void main(String[] args) {

		String hostname = args.length > 0 ? args[0] : "localhost";

		try (DatagramSocket theSocket = new DatagramSocket()) {
			InetAddress server = InetAddress.getByName(hostname);
			BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				String theLine = userInput.readLine();
				if (theLine.equals("."))
					break;
				byte[] data = theLine.getBytes();
				DatagramPacket theOutput = new DatagramPacket(data, data.length, server, PORT);
				theSocket.send(theOutput);
			} // end while
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
}