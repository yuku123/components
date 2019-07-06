package com.zifang.demo.jdk.java.io.nio;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/*
 * FILE���һЩ��صķ�������/
 */
public class FileDemo {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//File f=new File("c.txt");
		FileWriter fw=new FileWriter("out.txt");
		fw.write("aaa");
		//������йرղſ�����ȷд��
		fw.close();
		//���ͬ�����ļ����и���
		FileOutputStream fwout=new FileOutputStream("out1.txt");
		//PrintStream ps=new PrintStream(fwout);
		//ps.print("�Է�");
		//f.createNewFile();
		//System.out.print(f.getAbsolutePath());

	}

}
