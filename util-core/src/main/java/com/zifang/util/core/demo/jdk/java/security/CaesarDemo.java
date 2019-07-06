package com.zifang.util.core.demo.jdk.java.security;

/**
 * 凯撒密码是罗马扩张时期朱利斯• 凯撒（Julius Caesar）创造的，用于加密通过信使传
 * 递的作战命令。它将字母表中的字母移动一定位置而实现加密。例如如果向右移动 2 位，则 字母 A 将变为 C，字母 B 将变为 D，…，字母 X 变成
 * Z，字母 Y 则变为 A，字母 Z 变为 B。 因此，假如有个明文字符串“Hello”用这种方法加密的话，将变为密文： “Jgnnq” 。而如果
 * 要解密，则只要将字母向相反方向移动同样位数即可。如密文“Jgnnq”每个字母左移两位 变为“Hello” 。这里，移动的位数“2”是加密和解密所用的密钥。
 */
public class CaesarDemo {
	public static void main(String args[]) throws Exception {
		String s = args[0];
		s = "1234567890abcd";
		int key = Integer.parseInt(args[1]);
		String es = "";
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 'a' && c <= 'z') // 是小写字母
			{
				c += key % 26; // 移动 key%26 位
				if (c < 'a')
					c += 26; // 向左超界
				if (c > 'z')
					c -= 26; // 向右超界
			} else if (c >= 'A' && c <= 'Z') // 是大写字母
			{
				c += key % 26;
				if (c < 'A')
					c += 26;
				if (c > 'Z')
					c -= 26;
			}
			es += c;
		}
		System.out.println(es);
	}
	
}
/*
java Caesar 明文（要加密的字符串） 密钥（移动的位数）即可加密。
在密钥前面加上负号，将运行java Caesar 明文（要加密的字符串） -密钥（移动的位数）即可解密。
如为了加密字符串“Hello World!” ，可随意取一个密钥如 4，运行：
	java Caesar "Hello World!" 4
将输出“Lipps Asvph!” 。这里“Hello World!”是明文， “Lipps Asvph!”是密文。
如果密钥大于 26，程序中移位前会和 26 取模而将其调整到 26 以下。因此运行：
	java Caesar "Hello World!" 30
同样将输出“Lipps Asvph!” 。
为了将密文“Lipps Asvph!”解密，需要知道加密该密文所用的密钥 4，这样，执行：
	java Caesar "Lipps Asvph!" -4
将得到明文“Hello World!” 。
如果密钥和加密时所用的不同，则解密时将得到无意义的输出，如运行
	java Caesar "Lipps Asvph!" –3
程序将输出“Ifmmp Xpsme!” 。这样，只有知道密钥才能得到原来的密文。
*/