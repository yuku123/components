package com.zifang.demo.jdk.java.net.bookdemo;

import java.io.*;
import java.net.*;

public class AllHeaders {

	public static void printHeaders(String url){
		try {
			URL u = new URL(url);
			URLConnection uc = u.openConnection();
			for (int j = 0;; j++) {
				String header = uc.getHeaderField(j);
				if (header == null)
					break;
				System.out.println(uc.getHeaderFieldKey(j) + ": " + header);
			}
		} catch (MalformedURLException ex) {
			System.err.println(url + " is not a URL I understand.");
		} catch (IOException ex) {
			System.err.println(ex);
		}
		System.out.println();
	}

	public static void main(String[] args) {
		printHeaders("http://www.infcn.com.cn");
	}
}