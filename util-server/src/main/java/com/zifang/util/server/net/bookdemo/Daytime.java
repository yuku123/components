package com.zifang.util.server.net.bookdemo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.ParseException;

public class Daytime {

	public static StringBuilder getDateFromNetwork() throws IOException, ParseException {
		try (Socket socket = new Socket("time.nist.gov", 13)) {
			socket.setSoTimeout(15000);
			InputStream in = socket.getInputStream();
			StringBuilder time = new StringBuilder();
			InputStreamReader reader = new InputStreamReader(in, "ASCII");
			for (int c = reader.read(); c != -1; c = reader.read()) {
				time.append((char) c);
			}
			return time;
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getDateFromNetwork().toString());
	}
}