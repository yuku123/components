package com.zifang.util.core.demo.jdk.java.lang;

import java.util.Arrays;


/**
 * 浅谈二进制、十进制、十六进制、字符串之间的相互转换
 *
 */
public class BinOctDecHexConverter {

	/**
	 * 1. 字节转10进制 
	 * 	直接使用(int)类型转换。
	 */
	public static int byte2Int(byte b) {
		int r = (int) b;
		return r;
	}

	/**
	 * 2. 10进制转字节
	 *  直接使用(byte)类型转换。
	 */
	public static byte int2Byte(int i) {
		byte r = (byte) i;
		return r;
	}

	/**
	 * 3. 字节数组转16进制字符串
	 * 对每一个字节，先和0xFF做与运算，然后使用Integer.toHexString()函数，如果结果只有1位，需要在前面加0。
	 * 
	 * @param b
	 * @return
	 */
	public static String bytes2HexString(byte[] b) {
		String r = "";

		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			r += hex.toUpperCase();
		}

		return r;
	}

	/**
	 * 4. 16进制字符串转字节数组
	 * 这个比较复杂，每一个16进制字符是4bit，一个字节是8bit，所以两个16进制字符转换成1个字节，对于第1个字符，
	 * 转换成byte以后左移4位，然后和第2个字符的byte做或运算，这样就把两个字符转换为1个字节。
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hexString2Bytes(String hex) {

		if ((hex == null) || (hex.equals(""))) {
			return null;
		} else if (hex.length() % 2 != 0) {
			return null;
		} else {
			hex = hex.toUpperCase();
			int len = hex.length() / 2;
			byte[] b = new byte[len];
			char[] hc = hex.toCharArray();
			for (int i = 0; i < len; i++) {
				int p = 2 * i;
				b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p + 1]));
			}
			return b;
		}

	}

	/*
	 * 字符转换为字节
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 5. 字节数组转字符串
	 *  直接使用new String()。
	 * 
	 * @param b
	 * @return
	 * @throws Exception
	 */
	public static String bytes2String(byte[] b) throws Exception {
		String r = new String(b, "UTF-8");
		return r;
	}

	/**
	 * 6. 字符串转字节数组 直接使用getBytes()。
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] string2Bytes(String s) {
		byte[] r = s.getBytes();
		return r;
	}

	/**
	 * 7. 16进制字符串转字符串
	 *  先转换成byte[]，再转换成字符串。
	 * 
	 * @param hex
	 * @return
	 * @throws Exception
	 */
	public static String hex2String(String hex) throws Exception {
		String r = bytes2String(hexString2Bytes(hex));
		return r;
	}

	/**
	 * 8. 字符串转16进制字符串
	 *  先转换为byte[]，再转换为16进制字符串。
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	public static String string2HexString(String s) throws Exception {
		String r = bytes2HexString(string2Bytes(s));
		return r;
	}

	public static void main(String[] args) throws Exception {
		byte b1 = (byte) 45;
		System.out.println("1.字节转10进制:" + byte2Int(b1));

		int i = 89;
		System.out.println("2.10进制转字节:" + int2Byte(i));

		byte[] b2 = new byte[] { (byte) 0xFF, (byte) 0x5F, (byte) 0x6, (byte) 0x5A };
		System.out.println("3.字节数组转16进制字符串:" + bytes2HexString(b2));

		String s1 = new String("1DA47C");
		System.out.println("4.16进制字符串转字节数组:" + Arrays.toString(hexString2Bytes(s1)));

		System.out.println("5.字节数组转字符串:" + bytes2String(b2));

		System.out.println("6.字符串转字节数组:" + Arrays.toString(string2Bytes(s1)));

		System.out.println("7.16进制字符串转字符串:" + hex2String(s1));

		String s2 = new String("Hello!");
		System.out.println("8.字符串转16进制字符串:" + string2HexString(s2));
	}
}
