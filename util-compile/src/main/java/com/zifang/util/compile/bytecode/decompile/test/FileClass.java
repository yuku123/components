package com.zifang.util.compile.bytecode.decompile.test;


import javax.swing.*;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;

public class FileClass {
	
//	private List<Map<Integer, List<Map<String, String>>>> niubi;
	public FileClass() {
		
	}
	
	public FileClass(JFrame obj, JFrame jFrame) {
		
	}

	private void hello() {
		
	}
	
	public static void main(String[] args) {
		

		// new 对象
		String bbb = new String("123qwe");
		String ccc = new String();
		String string = new String("oieurtwe");
		string.compareToIgnoreCase("asdfasdf");
		new String();
		new String("123123123");
		new StringCharacterIterator("asdfasdf", 123, 321, 12);
		new JFrame(new String("123123"));
		new String(new String(new String("asdfasdfasdf")));
		
		// 方法调用
		String string2=string.concat(string);
		new JFrame(new String("asdfasdfas")).getContentPane();
		FileClass fileClass = new FileClass(new JFrame(), new JFrame(new String("qwetrytetw")));
		FileClass fileClass3 = new FileClass();
		
		// 调用私有方法
		fileClass3.hello();
		
		// 调用接口方法 
		FileClass fileClass1 = new FileClass();
		List<FileClass> aaa = new ArrayList<>();
		aaa.add(fileClass);
		
		// 调用静态方法
		System.out.println("asdfasdf");
//		new String(StringUtils.chomp("adsfasdfasdf"));
	}

}