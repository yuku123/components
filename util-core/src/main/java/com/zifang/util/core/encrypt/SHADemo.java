package com.zifang.util.core.encrypt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * SHA(Secure Hash Algorithm)安全散列算法数字签名等密码学应用中重要的工具，
 */
public class SHADemo {

    /***
     * SHA加密 生成40位SHA码
     *
     * @param inStr 待加密字符串
     * @return 返回40位SHA码
     */
    public static String shaEncode(String inStr) throws Exception {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = inStr.getBytes(StandardCharsets.UTF_8);
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * 测试主函数
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String str = "amigoxiexiexingxing";
        System.out.println("原始：" + str);
        System.out.println("SHA后：" + shaEncode(str));
    }
}