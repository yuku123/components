package com.zifang.util.algrithm.security;

import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.Date;

/**
 * RSA 是一种非对称加密算法，一般很难破解，因此一些要求比较高的系统通常会采用rsa加密算法，一般来说用RSA加密有如下几个步骤.
 * 1. 生成公钥与私钥
 * 2. 用公钥对需要加密的字符串等进行加密
 * 3. 在需要解密的地方，用私钥进行解密
 */
//1. 生成公钥与私钥
public class GenKeys {
    public static void main(String[] args) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA"); 
        SecureRandom secureRandom = new SecureRandom(new Date().toString().getBytes());
        keyPairGenerator.initialize(1024, secureRandom);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        String publicKeyFilename = "D:/publicKeyFile";
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        FileOutputStream fos = new FileOutputStream(publicKeyFilename); 
        fos.write(publicKeyBytes); 
        fos.close();
        String privateKeyFilename = "D:/privateKeyFile"; 
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        fos = new FileOutputStream(privateKeyFilename); 
        fos.write(privateKeyBytes); 
        fos.close();
    }
}