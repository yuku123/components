package com.zifang.demo.jdk.java.io.bio;

import java.io.*;
import java.util.Enumeration;
import java.util.Vector;

public class SequenceInputStreamDemo {
	public static void sequenceStream() throws IOException {
		// 创建字节输入流对象s1,s2,s3
		InputStream s1 = new FileInputStream(new File("src/chapter8/file/1.txt"));
		InputStream s2 = new FileInputStream(new File("src/chapter8/file/2.txt"));
		InputStream s3 = new FileInputStream(new File("src/chapter8/file/3.txt"));

		/**
		 * SequenceInputStream(Enumeration<? extends InputStream> e)
		 * 通过记住参数来初始化新创建的 SequenceInputStream， 该参数必须是生成运行时类型为 InputStream 对象的
		 * Enumeration 型参数。
		 */
		// 创建一个Vector类对象v
		Vector<InputStream> v = new Vector<>();
		/**
		 * void addElement(E obj) 将指定的组件添加到此向量的末尾，将其大小增加 1。
		 */
		// 将3个字节流对象添加到Vector容器中
		v.addElement(s1);
		v.addElement(s2);
		v.addElement(s3);
		// 获取Vector对象中的元素
		Enumeration<InputStream> e = v.elements();
		// 将Enumeration对象中的流合并（创建一个序列流，用于合并多个字节流文件s1,s2,s3）
		SequenceInputStream se = new SequenceInputStream(e);
		OutputStream os = new FileOutputStream("src/chapter8/file/123.txt");
		byte[] b = new byte[1024];
		int len = 0;
		while ((len = se.read(b)) != -1) {
			os.write(b, 0, len);
			os.write("\r\n".getBytes());
		}
		System.out.println("合并成功");
	}

	public static void main(String[] args) throws IOException {
		sequenceStream();
	}
}
