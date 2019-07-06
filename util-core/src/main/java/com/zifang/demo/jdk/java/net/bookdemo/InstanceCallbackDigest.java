package com.zifang.demo.jdk.java.net.bookdemo;

import java.io.*;
import java.security.*;

public class InstanceCallbackDigest implements Runnable {

	private String filename;
	private InstanceCallbackDigestUserInterface callback;

	public InstanceCallbackDigest(String filename, InstanceCallbackDigestUserInterface callback) {
		this.filename = filename;
		this.callback = callback;
	}

	@Override
	public void run() {
		try {
			FileInputStream in = new FileInputStream(filename);
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			DigestInputStream din = new DigestInputStream(in, sha);
			while (din.read() != -1)
				; // read entire file
			din.close();
			byte[] digest = sha.digest();
			callback.receiveDigest(digest);
		} catch (IOException | NoSuchAlgorithmException ex) {
			System.err.println(ex);
		}
	}
}