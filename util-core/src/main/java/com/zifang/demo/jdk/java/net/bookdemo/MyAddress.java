package com.zifang.demo.jdk.java.net.bookdemo;

import java.net.*;

public class MyAddress {

	public static void main(String[] args) {
		try {
			InetAddress address = InetAddress.getLocalHost();
			System.out.println(address);
		} catch (UnknownHostException ex) {
			System.out.println("Could not find this computer's address.");
		}
	}
}
