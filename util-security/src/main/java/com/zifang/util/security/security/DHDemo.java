package com.zifang.util.security.security;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;


//非对称算法 Diffie-Hellman  人名，密钥交换算法

/**
 * DH 算法中，A 可以用自己的密钥和 B 的公钥按照一定方法生成一个密钥，B 也可以用 自己的密钥和 A
 * 的公钥按照一定方法生成一个密钥，由于一些数学规律，这两个密钥完全 相同。这样，A 和 B 间就有了一个共同的密钥可以用于各种加密。本实例介绍 Java
 * 中在上 一小节的基础上如何利用 DH 公钥和私钥各自创建共享密钥。
 * 
 * @Company: Beijing INFCN Software Co.,Ltd.
 * @ClassName: DHDemo
 * @Project: jdk_demo
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author cc
 * @date 2016年11月23日 下午4:30:33
 * @version 1.0
 */
public class DHDemo {

	public static String src = "infcn";

	public static void main(String[] args) {
		jdkDH();
	}

	public static void jdkDH() {
		try {
			// 1、初始化发送方密钥
			KeyPairGenerator senderKeyPairGenerator = KeyPairGenerator.getInstance("DH");
			senderKeyPairGenerator.initialize(512);
			KeyPair senderKeyPair = senderKeyPairGenerator.generateKeyPair();
			// 发送方公钥，发送给接收方（网络、文件...）
			byte[] senderPublicKeyEnc = senderKeyPair.getPublic().getEncoded();

			// 2、初始化接收方密钥
			KeyFactory receiverKeyFactory = KeyFactory.getInstance("DH");
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(senderPublicKeyEnc);
			PublicKey receiverPublicKey = receiverKeyFactory.generatePublic(x509EncodedKeySpec);
			DHParameterSpec dhParameterSpec = ((DHPublicKey) receiverPublicKey).getParams();
			KeyPairGenerator receiverKeyPairGenerator = KeyPairGenerator.getInstance("DH");
			receiverKeyPairGenerator.initialize(dhParameterSpec);
			KeyPair receiverKeyPair = receiverKeyPairGenerator.generateKeyPair();
			PrivateKey receiverPrivateKey = receiverKeyPair.getPrivate();
			byte[] receiverPublicKeyEnc = receiverKeyPair.getPublic().getEncoded();

			// 3、密钥构建
			KeyAgreement receiverKeyAgreement = KeyAgreement.getInstance("DH");
			receiverKeyAgreement.init(receiverPrivateKey);
			receiverKeyAgreement.doPhase(receiverPublicKey, true);
			SecretKey receiverDesKey = receiverKeyAgreement.generateSecret("DES");

			KeyFactory senderKeyFactory = KeyFactory.getInstance("DH");
			x509EncodedKeySpec = new X509EncodedKeySpec(receiverPublicKeyEnc);
			PublicKey senderPublicKey = senderKeyFactory.generatePublic(x509EncodedKeySpec);
			KeyAgreement senderKeyAgreement = KeyAgreement.getInstance("DH");
			senderKeyAgreement.init(senderKeyPair.getPrivate());
			senderKeyAgreement.doPhase(senderPublicKey, true);

			SecretKey senderDesKey = senderKeyAgreement.generateSecret("DES");

			if (Objects.equals(receiverDesKey, senderDesKey)) {
				System.out.println("双方密钥相同");
			}

			// 4、加密
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, senderDesKey);
			byte[] result = cipher.doFinal(src.getBytes());
			//System.out.println("jdk dh encrypt : " + Base64.encodeBase64String(result));

			// 5、解密
			cipher.init(Cipher.DECRYPT_MODE, receiverDesKey);
			result = cipher.doFinal(result);
			System.out.println("jdk dh encrypt : " + new String(result));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
