package com.zifang.util.core.lang.primitive;

/**
 * @author: zifang
 * @time: 2021-11-23 16:24:00
 * @description: byte util
 * @version: JDK 1.8
 */
public class ByteUtil {

    /**
     * @author: zifang
     * @description: merge multiple byte array
     * @time: 2021-11-22 13:21
     * @params: [a, b] request
     * @return: byte[] response
     */
    public static byte[] merge(byte[] origin, byte[]... extra) {
        if (null == origin) {
            throw new RuntimeException("Origin data is null");
        }
        int newLength = origin.length;
        if (extra.length > 0) {
            for (byte[] bytes : extra) {
                if (null != bytes) {
                    newLength += bytes.length;
                }
            }
        }
        byte[] result = new byte[newLength];
        int position = origin.length;
        System.arraycopy(origin, 0, result, 0, position);
        if (extra.length > 0) {
            for (byte[] bytes : extra) {
                if (null != bytes) {
                    int currentLength = bytes.length;
                    System.arraycopy(bytes, 0, result, position, currentLength);
                    position += currentLength;
                }
            }
        }

        return result;
    }

    public static byte[] rightPaddingZero(byte[] data, int length) {
        if (length == 0) {
            throw new RuntimeException("Length don't allow is zero");
        }
        byte[] dataByte = data;

        if (data.length % length != 0) {
            byte[] blankBytes = new byte[length - data.length % length];
            dataByte = merge(data, blankBytes);
        }
        return dataByte;
    }


    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            int value = b & 0xFF;
            String hexValue = Integer.toHexString(value);
            if (hexValue.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hexValue);
        }
        return stringBuilder.toString();
    }


    public static byte[] hexStringToBytes(String hexString) {
        if (null == hexString || hexString.isEmpty()) {
            return null;
        }
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() >> 1];
        int index = 0;
        for (int i = 0; i < hexString.length(); i++) {
            if (index > hexString.length() - 1) {
                return byteArray;
            }
            byte highDit = (byte) (Character.digit(hexString.charAt(index), 16) & 0xFF);
            byte lowDit = (byte) (Character.digit(hexString.charAt(index + 1), 16) & 0xFF);
            byteArray[i] = (byte) (highDit << 4 | lowDit);
            index += 2;
        }
        return byteArray;
    }

    public static Byte parseByte(Object object) {
        if (null == object) {
            return null;
        }
        return Byte.parseByte(object.toString());
    }

    public static Byte parseByteOrDefault(Object object, Byte defaultValue) {
        if (null == object) {
            return defaultValue;
        }
        return Byte.parseByte(object.toString());
    }

}
