package com.zifang.util.zex.security;

import javax.crypto.Cipher;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * 备注，这里记录的只是测试方法，当然在实际使用过程中，可能还需要 对加密后的 byte[] 采用 base64
 * 编码，转换成字符串存储起来，在解密的时候，先通过 base64 还原成 byte, 然后在解密，这样会更好
 */
public class TestEncryptAndDecrypt {
	public static void main(String[] args) throws Exception {
		String input = "thisIsMyPassword$7788";
		Cipher cipher = Cipher.getInstance("RSA");
		RSAPublicKey pubKey = (RSAPublicKey) PublicKeyReader.get("d:/publicKeyFile");
		RSAPrivateKey privKey = (RSAPrivateKey) PrivateKeyReader.get("d:/privateKeyFile");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		byte[] cipherText = cipher.doFinal(input.getBytes());
		// 加密后的东西
		System.out.println("cipher: " + new String(cipherText));
		// 开始解密
		cipher.init(Cipher.DECRYPT_MODE, privKey);
		byte[] plainText = cipher.doFinal(cipherText);
		System.out.println("plain : " + new String(plainText));
	}

}