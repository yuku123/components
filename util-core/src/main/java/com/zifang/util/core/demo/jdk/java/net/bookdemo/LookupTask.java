package com.zifang.util.core.demo.jdk.java.net.bookdemo;

import java.net.*;
import java.util.concurrent.Callable;

public class LookupTask implements Callable<String> {

	private String line;

	public LookupTask(String line) {
		this.line = line;
	}

	@Override
	public String call() {
		try {
			// separate out the IP address
			int index = line.indexOf(' ');
			String address = line.substring(0, index);
			String theRest = line.substring(index);
			String hostname = InetAddress.getByName(address).getHostName();
			return hostname + " " + theRest;
		} catch (Exception ex) {
			return line;
		}
	}
}