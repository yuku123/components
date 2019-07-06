package com.zifang.demo.jdk.java.net.bookdemo;

import java.io.*;
import java.security.*;

public class ReturnDigest extends Thread {

	private String filename;
	private byte[] digest;

	public ReturnDigest(String filename) {
		this.filename = filename;
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
			digest = sha.digest();
		} catch (IOException ex) {
			System.err.println(ex);
		} catch (NoSuchAlgorithmException ex) {
			System.err.println(ex);
		}
	}

	public byte[] getDigest() {
		return digest;
	}
}