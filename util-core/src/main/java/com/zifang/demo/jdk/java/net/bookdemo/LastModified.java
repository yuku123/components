package com.zifang.demo.jdk.java.net.bookdemo;

import java.io.*;
import java.net.*;
import java.util.*;

public class LastModified {

	public static void main(String[] args) {
		args = new String[] { "http://www.suntray.com" };
		for (int i = 0; i < args.length; i++) {
			try {
				URL u = new URL(args[i]);
				HttpURLConnection http = (HttpURLConnection) u.openConnection();
				http.setRequestMethod("HEAD");
				System.out.println(u + " was last modified at " + new Date(http.getLastModified()));
			} catch (MalformedURLException ex) {
				System.err.println(args[i] + " is not a URL I understand");
			} catch (IOException ex) {
				System.err.println(ex);
			}
			System.out.println();
		}
	}
}