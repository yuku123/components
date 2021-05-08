package com.zifang.util.core.encrypt;

import org.apache.commons.codec.binary.Hex;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

//数字签名

/**
 * 数字签名  带有密钥（公钥、私钥）的消息摘要算法
 * 验证数据完整性、认证数据来源、抗否认性
 * 
 * 对明文进行加密，有两种情况需要这样作： 
1、您向朋友传送加密数据，您希望只有您的朋友可以解密，这样的话，您需要首先获取您朋友的密钥对中公开的那一个密钥，e及n。然后用这个密钥进行加密，这样密文只有您的朋友可以解密，因为对应的私钥只有您朋友拥有。 
2、您向朋友传送一段数据附加您的数字签名，您需要对您的数据进行MD5之类的运算以取得数据的"指纹"，再对"指纹"进行加密，加密将使用您自己的密钥对中的不公开的私钥。您的朋友收到数据后，用同样的运算获得数据指纹，再用您的公钥对加密指纹进行解密，比较解密结果与他自己计算出来的指纹是否一致，即可确定数据是否的确是您发送的、以及在传输过程中是否被篡改。 
 *
 *
 */
public class RSA2Demo {

	private static String src = "infcn";
	
	public static void main(String[] args) {
		jdkRSA();
	}
	
	public static void jdkRSA(){
		try {
			//1、初始化密钥
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(512);//64的整倍数
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			RSAPublicKey rsaPublicKey = (RSAPublicKey)keyPair.getPublic(); 
			RSAPrivateKey rsaPrivateKey = (RSAPrivateKey)keyPair.getPrivate();
			//System.out.println("Public Key : "+ Base64.encodeBase64String(rsaPublicKey.getEncoded()));
			//System.out.println("Private Key : "+ Base64.encodeBase64String(rsaPrivateKey.getEncoded()));
			
			//2、签名
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			Signature signature = Signature.getInstance("MD5withRSA");
			signature.initSign(privateKey);
			signature.update(src.getBytes());
			byte[] result = signature.sign();
			System.out.println("jdk rsa sign : "+ Hex.encodeHexString(result));
			
			
			//3、验证签名
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
			keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
			signature = Signature.getInstance("MD5withRSA");
			signature.initVerify(publicKey);
			signature.update(src.getBytes());
			boolean bool = signature.verify(result);
			System.out.println("jdk rsa verify : "+ bool);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
