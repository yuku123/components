package com.zifang.util.core.demo.jdk.java.net.bookdemo;

import java.net.*;

public class ReverseTest {

	public static void main(String[] args) throws UnknownHostException {
		InetAddress ia = InetAddress.getByName("208.201.239.100");
		System.out.println(ia.getCanonicalHostName());
	}
}
