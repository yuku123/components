package com.zifang.util.core.encrypt;//package com.zifang.util.zex.security;
//
//import org.apache.commons.codec.binary.Hex;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//
//import javax.crypto.Cipher;
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import javax.crypto.SecretKeyFactory;
//import javax.crypto.spec.DESedeKeySpec;
//import java.security.SecureRandom;
//import java.security.Security;
//
////对称加密
//public class DES3Demo {
//
//	private static String src = "infcn";
//
//	public static void main(String[] args) {
//		jdk3DES();
//
//		bc3DES();
//	}
//	public static void jdk3DES(){
//		try {
//			//生成KEY
//			KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
////			keyGenerator.init(168);
//			keyGenerator.init(new SecureRandom());//生成默认长度的key
//			SecretKey secretKey = keyGenerator.generateKey();
//			byte[] bytesKey = secretKey.getEncoded();
//			System.out.println("---"+new String(bytesKey));
//			//KEY转换
//			DESedeKeySpec desKeySpec = new DESedeKeySpec(bytesKey);
//			SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
//			SecretKey convertSecretKey = factory.generateSecret(desKeySpec);
//
//			//加密
//			Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");	//算法/工作方式/填充方式
//			cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
//			byte[] result = cipher.doFinal(src.getBytes());
//			System.out.println("jdk 3des encrypt : "+Hex.encodeHexString(result));
//
//			cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);
//			result = cipher.doFinal(result);
//			System.out.println("jdk 3des decrypt : "+ new String(result));
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void bc3DES(){
//		try {
//			Security.addProvider(new BouncyCastleProvider());
//
//			//生成KEY
//			KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede","BC");
//			keyGenerator.getProvider();
////			keyGenerator.init(192);
//			keyGenerator.init(new SecureRandom());//生成默认长度的key
//			SecretKey secretKey = keyGenerator.generateKey();
//			byte[] bytesKey = secretKey.getEncoded();
//
//			//KEY转换
//			DESedeKeySpec desKeySpec = new DESedeKeySpec(bytesKey);
//			SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
//			SecretKey convertSecretKey = factory.generateSecret(desKeySpec);
//
//			//加密
//			Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");	//算法/工作方式/填充方式
//			cipher.init(Cipher.ENCRYPT_MODE, convertSecretKey);
//			byte[] result = cipher.doFinal(src.getBytes());
//			System.out.println("bc 3des encrypt : "+Hex.encodeHexString(result));
//
//			cipher.init(Cipher.DECRYPT_MODE, convertSecretKey);
//			result = cipher.doFinal(result);
//			System.out.println("bc 3des decrypt : "+ new String(result));
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//}
