package com.zifang.demo.jdk.java.net.bookdemo;

import java.io.*;
import java.net.*;

public class ContentGetter {

	public static void main(String[] args) {

		if (args.length > 0) {
			// Open the URL for reading
			try {
				URL u = new URL(args[0]);
				Object o = u.getContent();
				System.out.println("I got a " + o.getClass().getName());
			} catch (MalformedURLException ex) {
				System.err.println(args[0] + " is not a parseable URL");
			} catch (IOException ex) {
				System.err.println(ex);
			}
		}
	}
}