package com.zifang.util.compile.bytecode.resolver.parser.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class InputStreams {
	
	public static FileInputStream fileInputStream(File file) {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	} 

}
