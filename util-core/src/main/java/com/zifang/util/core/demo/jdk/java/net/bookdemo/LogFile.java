package com.zifang.util.core.demo.jdk.java.net.bookdemo;

import java.io.*;
import java.util.*;

public class LogFile {

	private Writer out;

	public LogFile(File f) throws IOException {
		FileWriter fw = new FileWriter(f);
		this.out = new BufferedWriter(fw);
	}

	public void writeEntry(String message) throws IOException {
		Date d = new Date();
		out.write(d.toString());
		out.write('\t');
		out.write(message);
		out.write("\r\n");
	}

	public void close() throws IOException {
		out.flush();
		out.close();
	}
}