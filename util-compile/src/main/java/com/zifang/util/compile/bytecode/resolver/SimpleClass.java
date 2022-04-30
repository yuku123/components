package com.zifang.util.compile.bytecode.resolver;

import javax.annotation.Resource;
import javax.xml.bind.annotation.XmlElement;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public abstract class SimpleClass {

	@Resource(name = "名字是a")
	@XmlElement(name = "root")
	private final static int a = 1;
	
	public static void main(String[] args) {
		try {
			new FileInputStream("D:\\");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public abstract String print(int a, int b);

}
