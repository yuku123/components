//package com.zifang.util.zex.security;
//
//import org.apache.commons.codec.binary.Hex;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//
//import javax.crypto.Cipher;
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import javax.crypto.SecretKeyFactory;
//import javax.crypto.spec.DESKeySpec;
//import java.security.Security;
//
////对称加密  本地数据，安全级别低    DES全称为Data Encryption Standard
//public class DESDemo {
//	private static String src = "infcn";
//	public static void main(String[] args) {
//		jdkDES();
//		System.out.println("-----------");
//		bcDES();
//	}
//	public static void jdkDES(){
//		try {
//			//生成KEY
//			KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
//			keyGenerator.init(56);
//			SecretKey secretKey = keyGenerator.generateKey();
//			byte[] bytesKey = secretKey.getEncoded();
//			System.out.println("---"+new String(bytesKey));
//			//KEY转换
//			DESKeySpec desKeySpec = new DESKeySpec(bytesKey);
//			SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
//			SecretKey convertSecretKey = factory.generateSecret(desKeySpec);
//
//			//加密
//			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");	//算法/工作方式/填充方式
//			cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
//			byte[] result = cipher.doFinal(src.getBytes());
//			System.out.println("jdk des encrypt : "+Hex.encodeHexString(result));
//
//			cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);
//			result = cipher.doFinal(result);
//			System.out.println("jdk des decrypt : "+ new String(result));
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void bcDES(){
//		try {
//			Security.addProvider(new BouncyCastleProvider());
//
//			//生成KEY
//			KeyGenerator keyGenerator = KeyGenerator.getInstance("DES","BC");
//			keyGenerator.getProvider();
//			keyGenerator.init(56);
//			SecretKey secretKey = keyGenerator.generateKey();
//			byte[] bytesKey = secretKey.getEncoded();
//
//			//KEY转换
//			DESKeySpec desKeySpec = new DESKeySpec(bytesKey);
//			SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
//			SecretKey convertSecretKey = factory.generateSecret(desKeySpec);
//
//			//加密
//			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");	//算法/工作方式/填充方式
//			cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
//			byte[] result = cipher.doFinal(src.getBytes());
//			System.out.println("bc des encrypt : "+Hex.encodeHexString(result));
//
//			cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);
//			result = cipher.doFinal(result);
//			System.out.println("bc des decrypt : "+ new String(result));
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//}
