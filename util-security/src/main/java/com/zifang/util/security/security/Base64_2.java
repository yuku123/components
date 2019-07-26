package com.zifang.util.security.security;

import java.io.IOException;

public class Base64_2 {

	/**
	 * 编码
	 * 
	 * @param bstr
	 * @return String
	 */
	public static String encode(byte[] bstr) {
		return new sun.misc.BASE64Encoder().encode(bstr);
	}

	/**
	 * 解码
	 * 
	 * @param str
	 * @return string
	 */
	public static byte[] decode(String str) {
		byte[] bt = null;
		try {
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			bt = decoder.decodeBuffer(str);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bt;
	}
	
	public static void main(String[] args) throws Exception {
		String a = "111";
		String b = encode(a.getBytes());
		System.out.println(b);
		System.out.println(new String(decode(b)));
	}
}
