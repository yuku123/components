package com.zifang.util.sensitive;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 掩码实现。
 */
public final class MaskUtil {

    private static final char STAR = '*';

    private MaskUtil() {
    }

    public static String mask(String value, SensitiveType type, MaskStyle style) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        switch (style) {
            case HASH:
                return hash(value);
            case HEAD_TAIL:
                return headTail(value, type);
            case FULL:
            default:
                return repeat(STAR, value.length());
        }
    }

    private static String headTail(String value, SensitiveType type) {
        // 邮箱按整体打码会变成 a***@e******.com，域名部分才是有用信息，单独处理
        if (type == SensitiveType.EMAIL) {
            int at = value.indexOf('@');
            if (at <= 0) {
                return repeat(STAR, value.length());
            }
            String local = value.substring(0, at);
            String domain = value.substring(at);
            return local.charAt(0) + repeat(STAR, Math.max(1, local.length() - 1)) + domain;
        }
        int head = type == null ? 2 : type.defaultHead();
        int tail = type == null ? 2 : type.defaultTail();
        if (head + tail >= value.length()) {
            return repeat(STAR, value.length());
        }
        StringBuilder sb = new StringBuilder(value.length());
        sb.append(value, 0, head);
        for (int i = head; i < value.length() - tail; i++) {
            sb.append(STAR);
        }
        sb.append(value, value.length() - tail, value.length());
        return sb.toString();
    }

    private static String hash(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(8);
            for (int i = 0; i < 4; i++) {
                sb.append(String.format("%02x", bytes[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is required by the JDK spec but is missing", e);
        }
    }

    private static String repeat(char c, int count) {
        char[] chars = new char[Math.max(0, count)];
        java.util.Arrays.fill(chars, c);
        return new String(chars);
    }
}
