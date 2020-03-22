package com.zifang.util.zex.security;

public class BASE32 {
	private final static String BASE32Str = "ABCDEFGHIJKLMNPQRSTUVWXYZ3456789";

	// 挂表，通过 BASE32[i] 和 BASE32.IndexOf(c) 获取某数字代表的字符和某字符代表的数字

	public static String encode(byte[] value) {
		// 编码
		int length = (value.length * 8 + 4) / 5;
		// 计算字符串的长度，每个 byte 为8位二进制数，1到5位二进制数需要1个字符，6到10位为2个，依此类推
		StringBuilder sb = new StringBuilder(length);
		// 通过 StringBuilder 预先分配空间，提高效率
		int cur_i = 0;
		// 当前 byte[] 索引
		byte cur_b = 0;
		// 当前 byte
		int index;
		// 5位二进制数结果
		int k;
		// 当前二进制位索引
		for (int i = 0; i < length; i++) {
			// 对每个 Base32 字符循环
			index = 0;
			// 初始置 0
			for (int j = 0; j < 5; j++) {
				// 对每个 Base32 字符代表的二进制位循环
				k = i * 5 + j;
				// 计算当前二进制位索引
				if (k == value.length * 8)
					break;
				// 二进制位扫描结束
				if (k % 8 == 0)
					cur_b = value[cur_i++];
				// 转移到下一个 byte，初始化时转移到第 0 个 byte
				index <<= 1;
				// 左移一位以便继续设置最低位
				index |= (cur_b & 128) == 0 ? 0 : 1;
				// 将 byte 的最高位送入 index 的最低位
				cur_b <<= 1;
				// 将 byte 左移一位以将次高位变为最高位
			}
			sb.append(BASE32Str.charAt(index));
			// 追加字符
		}
		return sb.toString();
	}

	public static byte[] decode(String value) {
		// 解码
		value = value.toUpperCase();
		// 转换为小写
		int length = value.length() * 5 / 8;
		// 计算长度，因为末位可能会多表示 0 到 4 个二进制位，因此无需修正余数。
		byte[] r = new byte[length];
		// 分配空间
		int cur_i = 0;
		// 当前 Base32 字符索引
		int cur_v = 0;
		// 当前 Base32 字符代表的数字
		int k;
		// 当前二进制位索引
		for (int i = 0; i < length; i++) {
			// 对每个 byte 循环
			for (int j = 0; j < 8; j++) {
				// 对每个 byte 对应的二进制位循环
				k = i * 8 + j;
				// 计算当前二进制位索引
				if (k == value.length() * 5)
					break;
				// 二进制位扫描结束，通常 Base32 字符代表的二进制位会大于等于实际长度
				// 因此此处主要用于检测不规范 Base32 编码
				if (k % 5 == 0) {
					cur_v = BASE32Str.indexOf(value.charAt(cur_i++));
					// 转移到下一个 Base32 字符，初始化时转移到第 0 个字符
					if (cur_i == value.length() && value.length() % 8 != 0)
						cur_v <<= value.length() * 5 % 8;
					// 根据 Base32 字符串代表的长度和实际 byte[] 长度
					// 修正最末尾 Base32 字符代表的数字。
				}
				r[i] <<= 1;
				r[i] |= (byte) ((cur_v & 16) == 0 ? 0 : 1);
				cur_v <<= 1;
				// 编码过程的逆过程，同样是移位、送位
			}
		}
		return r;
	}

	public static void main(String[] args) {
		byte[] numArray = new byte[10];
		int num1 = 32767;
		int num2 = 32768;
		int num3 = 65535;
		int num4 = 65535;
		int num5 = 65535;

		System.out.println("原始数据为："+num1 + "\t" + num2 + "\t" + num3 + "\t" + num4);

		numArray[0] = (byte) (num1 / 256);
		numArray[1] = (byte) (num1 % 256);
		numArray[2] = (byte) (num2 / 256);
		numArray[3] = (byte) (num2 % 256);
		numArray[4] = (byte) (num3 / 256);
		numArray[5] = (byte) (num3 % 256);
		numArray[6] = (byte) (num4 / 256);
		numArray[7] = (byte) (num4 % 256);
		numArray[8] = (byte) (num5 / 256);
		numArray[9] = (byte) (num5 % 256);

		String encryptStr = encode(numArray);
		
		//5个不超过65535的整数压缩后为：Q99ZAAH996ABP,Q99ZAAH996AB7999
		System.out.println("4个不超过65535的整数压缩后为："+encryptStr);

		byte[] dencrypt = decode(encryptStr);
		int n1 = valueOf(dencrypt[0] * 256 + (dencrypt[1] < 0 ? dencrypt[1] + 256 : dencrypt[1]));
		int n2 = valueOf(dencrypt[2] * 256 + (dencrypt[3] < 0 ? dencrypt[3] + 256 : dencrypt[3]));
		int n3 = valueOf(dencrypt[4] * 256 + (dencrypt[5] < 0 ? dencrypt[5] + 256 : dencrypt[5]));
		int n4 = valueOf(dencrypt[6] * 256 + (dencrypt[7] < 0 ? dencrypt[7] + 256 : dencrypt[7]));
		int n5 = valueOf(dencrypt[8] * 256 + (dencrypt[9] < 0 ? dencrypt[9] + 256 : dencrypt[9]));
		System.out.println("解密后为："+n1 + "\t" + n2 + "\t" + n3 + "\t" + n4 + "\t" + n5);
	}

	public static int valueOf(int num) {
		if (num < 0) {
			num += 65536;
		}
		return num;
	}

}
