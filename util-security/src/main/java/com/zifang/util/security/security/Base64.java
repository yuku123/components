package com.zifang.util.security.security;

import java.lang.reflect.Method;

/**
 *
 * https://blog.csdn.net/xx326664162/article/details/78122139
 *
 *
 * */
public class Base64 {

	/***
	 * encode by Base64
	 */
	public static String encodeBase64(byte[] input) throws Exception {
		Class clazz = Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
		Method mainMethod = clazz.getMethod("encode", byte[].class);
		mainMethod.setAccessible(true);
		Object retObj = mainMethod.invoke(null, new Object[] { input });
		return (String) retObj;
	}

	/***
	 * decode by Base64
	 */
	public static byte[] decodeBase64(String input) throws Exception {
		Class clazz = Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
		Method mainMethod = clazz.getMethod("decode", String.class);
		mainMethod.setAccessible(true);
		Object retObj = mainMethod.invoke(null, input);
		return (byte[]) retObj;
	}
	
	public static void main(String[] args) throws Exception {
		String a = "i love china";
		String b = encodeBase64(a.getBytes());
		System.out.println(b);
		System.out.println(new String(decodeBase64(b)));
	}
}
