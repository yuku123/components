package com.zifang.util.security.security;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 实现基本上就是这样，都是大同小异。不过，问题来了，结下来才是重点。 1.
 * RSA加密算法对于加密数据的长度是有要求的。一般来说，明文长度小于等于密钥长度（Bytes
 * ）-11。解决这个问题需要对较长的明文进行分段加解密，这个上面的代码已经实现了。 2.
 * 一旦涉及到双方开发，语言又不相同，不能够采用同一个工具的时候，切记要约定以下内容。 a）约定双方的BASE64编码
 * b）约定双方分段加解密的方式。我踩的坑也主要是这里
 * ，不仅仅是约定大家分段的大小，更重要的是分段加密后的拼装方式。doFinal方法加密完成后得到的仍然是byte
 * []，因为最终呈现的是编码后的字符串，所以你可以分段加密
 * ，分段编码和分段加密，一次编码两种方式（上面的代码采用的是后一种，也推荐采用这一种）。相信我不是所有人的脑回路都一样的
 * ，尤其是当他采用的开发语言和你不通时。**
 * 
 *
 *
 */
public class RSAUtils {

	public static final String CHARSET = "UTF-8";
	public static final String RSA_ALGORITHM = "RSA";

	public static void main(String[] args) throws Exception {
		Map<String, String> keyMap = RSAUtils.createKeys(1024);
		String publicKey = keyMap.get("publicKey");
		String privateKey = keyMap.get("privateKey");
		System.out.println("公钥: \n\r" + publicKey);
		System.out.println("私钥： \n\r" + privateKey);

		System.out.println("公钥加密——私钥解密");
		String str = "站在大明门前守卫的禁卫军，事先没有接到\n"
				+ "有关的命令，但看到大批盛装的官员来临，也就\n"
				+ "以为确系举行大典，因而未加询问。进大明门即\n"
				+ "为皇城。文武百官看到端门午门之前气氛平静，\n"
				+ "城楼上下也无朝会的迹象，既无几案，站队点名\n"
				+ "的御史和御前侍卫“大汉将军”也不见踪影，不免\n"
				+ "心中揣测，互相询问：所谓午朝是否讹传？2323232到了三季度累计跌幅拉丝机菲拉斯解放拉萨积分上篮得分纪录撒娇发牢骚的解放拉萨积分拉丝机菲拉斯解放拉萨积分洛杉矶地方扩大解放拉萨分33333333333333333333333333333333";
		System.out.println("\r明文：\r\n" + str);
		System.out.println("\r明文大小：\r\n" + str.getBytes().length);
		String encodedData = RSAUtils.publicEncrypt(str, RSAUtils.getPublicKey(publicKey));
		System.out.println("密文：\r\n" + encodedData);
		String decodedData = RSAUtils.privateDecrypt(encodedData, RSAUtils.getPrivateKey(privateKey));
		System.out.println("解密后文字: \r\n" + decodedData);

	}

	public static Map<String, String> createKeys(int keySize) {
		// 为RSA算法创建一个KeyPairGenerator对象
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
		}

		// 初始化KeyPairGenerator对象,密钥长度
		kpg.initialize(keySize);
		// 生成密匙对
		KeyPair keyPair = kpg.generateKeyPair();
		// 得到公钥
		Key publicKey = keyPair.getPublic();
		String publicKeyStr = Base64_3.encode(publicKey.getEncoded());
		// 得到私钥
		Key privateKey = keyPair.getPrivate();
		String privateKeyStr = Base64_3.encode(privateKey.getEncoded());
		Map<String, String> keyPairMap = new HashMap<String, String>();
		keyPairMap.put("publicKey", publicKeyStr);
		keyPairMap.put("privateKey", privateKeyStr);

		return keyPairMap;
	}

	/**
	 * 得到公钥
	 * 
	 * @param publicKey
	 *            密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// 通过X509编码的Key指令获得公钥对象
		KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64_3.decode(publicKey));
		RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
		return key;
	}

	/**
	 * 得到私钥
	 * 
	 * @param privateKey
	 *            密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException,
			InvalidKeySpecException {
		// 通过PKCS#8编码的Key指令获得私钥对象
		KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64_3.decode(privateKey));
		RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
		return key;
	}

	/**
	 * 公钥加密
	 * 
	 * @param data
	 * @param publicKey
	 * @return
	 */
	public static String publicEncrypt(String data, RSAPublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return Base64_3.encode(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey
					.getModulus().bitLength()));
		} catch (Exception e) {
			throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
		}
	}

	/**
	 * 私钥解密
	 * 
	 * @param data
	 * @param privateKey
	 * @return
	 */

	public static String privateDecrypt(String data, RSAPrivateKey privateKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64_3.decode(data), privateKey.getModulus()
					.bitLength()), CHARSET);
		} catch (Exception e) {
			throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
		}
	}

	/**
	 * 私钥加密
	 * 
	 * @param data
	 * @param privateKey
	 * @return
	 */

	public static String privateEncrypt(String data, RSAPrivateKey privateKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			return Base64_3.encode(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), privateKey
					.getModulus().bitLength()));
		} catch (Exception e) {
			throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
		}
	}

	/**
	 * 公钥解密
	 * 
	 * @param data
	 * @param publicKey
	 * @return
	 */

	public static String publicDecrypt(String data, RSAPublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64_3.decode(data), publicKey.getModulus()
					.bitLength()), CHARSET);
		} catch (Exception e) {
			throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
		}
	}

	private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
		int maxBlock = 0;
		if (opmode == Cipher.DECRYPT_MODE) {
			maxBlock = keySize / 8;
		} else {
			maxBlock = keySize / 8 - 11;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] buff;
		int i = 0;
		byte[] resultDatas;
		try {
			while (datas.length > offSet) {
				if (datas.length - offSet > maxBlock) {
					buff = cipher.doFinal(datas, offSet, maxBlock);
				} else {
					buff = cipher.doFinal(datas, offSet, datas.length - offSet);
				}
				out.write(buff, 0, buff.length);
				i++;
				offSet = i * maxBlock;
			}
			resultDatas = out.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultDatas;
	}

}