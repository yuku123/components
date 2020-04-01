package com.zifang.util.core.util;

/**
 * 字符串工具类，对字符串进行常规的处理
 */
public class StringUtil {

	public static String leftPad(String str, int size, char padChar) {
		if (str == null) {
			return null;
		} else {
			int pads = size - str.length();
			if (pads <= 0) {
				return str;
			} else {
				return pads > 8192 ? leftPad(str, size, String.valueOf(padChar)) : repeat(padChar, pads).concat(str);
			}
		}
	}

	public static String leftPad(String str, int size, String padStr) {
		if (str == null) {
			return null;
		} else {
			if (isEmpty(padStr)) {
				padStr = " ";
			}

			int padLen = padStr.length();
			int strLen = str.length();
			int pads = size - strLen;
			if (pads <= 0) {
				return str;
			} else if (padLen == 1 && pads <= 8192) {
				return leftPad(str, size, padStr.charAt(0));
			} else if (pads == padLen) {
				return padStr.concat(str);
			} else if (pads < padLen) {
				return padStr.substring(0, pads).concat(str);
			} else {
				char[] padding = new char[pads];
				char[] padChars = padStr.toCharArray();

				for(int i = 0; i < pads; ++i) {
					padding[i] = padChars[i % padLen];
				}

				return (new String(padding)).concat(str);
			}
		}
	}

	public static boolean isEmpty(CharSequence cs) {
		return cs == null || cs.length() == 0;
	}


	public static String repeat(char ch, int repeat) {
		if (repeat <= 0) {
			return "";
		} else {
			char[] buf = new char[repeat];

			for(int i = repeat - 1; i >= 0; --i) {
				buf[i] = ch;
			}

			return new String(buf);
		}
	}
	
	/**
	 * 将半角的符号转换成全角符号.(即英文字符转中文字符)
	 *
	 * @param str
	 * 			要转换的字符
	 * @return
	 */
	public static String changeToFull(String str) {
        String source = "1234567890!@#$%^&*()abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_=+\\|[];:'\",<.>/?";
        String[] decode = { "１", "２", "３", "４", "５", "６", "７", "８", "９", "０",
                "！", "＠", "＃", "＄", "％", "︿", "＆", "＊", "（", "）", "ａ", "ｂ",
                "ｃ", "ｄ", "ｅ", "ｆ", "ｇ", "ｈ", "ｉ", "ｊ", "ｋ", "ｌ", "ｍ", "ｎ",
                "ｏ", "ｐ", "ｑ", "ｒ", "ｓ", "ｔ", "ｕ", "ｖ", "ｗ", "ｘ", "ｙ", "ｚ",
                "Ａ", "Ｂ", "Ｃ", "Ｄ", "Ｅ", "Ｆ", "Ｇ", "Ｈ", "Ｉ", "Ｊ", "Ｋ", "Ｌ",
                "Ｍ", "Ｎ", "Ｏ", "Ｐ", "Ｑ", "Ｒ", "Ｓ", "Ｔ", "Ｕ", "Ｖ", "Ｗ", "Ｘ",
                "Ｙ", "Ｚ", "－", "＿", "＝", "＋", "＼", "｜", "【", "】", "；", "：",
                "'", "\"", "，", "〈", "。", "〉", "／", "？" };
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            int pos = source.indexOf(str.charAt(i));
            if (pos != -1) {
                result += decode[pos];
            } else {
                result += str.charAt(i);
            }
        }
        return result;
    }
	
	/**
	 *  将字符转换为编码为Unicode，格式 为'\u0020'<br>
	 * 		  unicodeEscaped(' ') = "\u0020"<br>
	 * 		  unicodeEscaped('A') = "\u0041"
	 *
	 * @param ch
	 * 			待转换的char 字符
	 * @return
	 */
	public static String unicodeEscaped(char ch) {
		if (ch < 0x10) {
			return "\\u000" + Integer.toHexString(ch);
		} else if (ch < 0x100) {
			return "\\u00" + Integer.toHexString(ch);
		} else if (ch < 0x1000) {
			return "\\u0" + Integer.toHexString(ch);
		}
		return "\\u" + Integer.toHexString(ch);
	}
	
	/**
	 * 进行toString操作，若为空，返回默认值
	 *
	 * @param object
	 * 				要进行toString操作的对象
	 * @param nullStr
	 * 				返回的默认值
	 * @return
	 */
	public static String toString(Object object, String nullStr){
		return object == null ? nullStr : object.toString();
	}


	/**
	 * 判断字符串是否全部都为小写
	 *
	 * @param value
	 * 				待判断的字符串
	 * @return
	 */
	public static boolean isAllLowerCase(String value){
		if(value == null || "".equals(value)){
			return false;
		}
		for (int i = 0; i < value.length(); i++) {
			if (Character.isLowerCase(value.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断字符串是否全部大写
	 *
	 * @param value
	 * 				待判断的字符串
	 * @return
	 */
	public static boolean isAllUpperCase(String value){
		if(value == null || "".equals(value)){
			return false;
		}
		for (int i = 0; i < value.length(); i++) {
			if (Character.isUpperCase(value.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断字符串是否为空
	 *
	 * @param string
	 * @return boolean
	 */
	public static boolean isEmptyString(String string){
		return string == null || string.length() == 0;
	}

	/**
	 * 判断字符串是否不为空
	 *
	 * @param string
	 * @return boolean
	 */
	public static boolean isNotEmptyString(String string){
		return string != null && string.length() > 0;
	}

}
