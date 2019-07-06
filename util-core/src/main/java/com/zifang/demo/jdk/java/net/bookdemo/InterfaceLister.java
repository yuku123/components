package com.zifang.demo.jdk.java.net.bookdemo;

import java.net.*;
import java.util.*;

public class InterfaceLister {

	public static void main(String[] args) throws SocketException {
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			NetworkInterface ni = interfaces.nextElement();
			if (ni.isUp()) {
				System.out.println(ni);
				byte addres[] = ni.getHardwareAddress();
				if (addres != null) {
					System.out.println(addres.length);
				}
			}
		}
	}
}
