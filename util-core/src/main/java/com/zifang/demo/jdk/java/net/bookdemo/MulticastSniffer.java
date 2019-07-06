package com.zifang.demo.jdk.java.net.bookdemo;

import java.io.*;
import java.net.*;

public class MulticastSniffer {

	public static void main(String[] args) {

		InetAddress group = null;
		int port = 0;

		// read the address from the command line
		try {
			group = InetAddress.getByName(args[0]);
			port = Integer.parseInt(args[1]);
		} catch (ArrayIndexOutOfBoundsException | NumberFormatException | UnknownHostException ex) {
			System.err.println("Usage: java MulticastSniffer multicast_address port");
			System.exit(1);
		}

		MulticastSocket ms = null;
		try {
			ms = new MulticastSocket(port);
			ms.joinGroup(group);

			byte[] buffer = new byte[8192];
			while (true) {
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
				ms.receive(dp);
				String s = new String(dp.getData(), "8859_1");
				System.out.println(s);
			}
		} catch (IOException ex) {
			System.err.println(ex);
		} finally {
			if (ms != null) {
				try {
					ms.leaveGroup(group);
					ms.close();
				} catch (IOException ex) {
				}
			}
		}
	}
}