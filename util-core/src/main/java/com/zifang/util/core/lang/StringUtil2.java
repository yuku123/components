package com.zifang.util.core.lang;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.zifang.util.core.Const.Symbol.*;
import static com.zifang.util.core.lang.regex.Patterns.FORMAT_PATTERN;
import static org.apache.logging.log4j.util.Strings.EMPTY;

public class StringUtil2 {
    private static final int INDEX_NOT_FOUND = -1;
    private static final int PAD_LIMIT = 8192;

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    /**
     * @author: zifang
     * @description: Judging that multiple strings are not empty
     * @description: 判断多个字符串都非空
     * @time: 2022-06-09 11:18:25
     * @params: [strArr] 字符串数组
     * @return: boolean 是否非空
     */
    public static boolean isNotEmpty(String... strArr) {
        for (String str : strArr) {
            if (isEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String s) {
        return !isBlank(s);
    }

    public static boolean isBlank(String s) {
        return null == s || s.trim().isEmpty();
    }

    public static boolean isEmpty(String s) {
        return null == s || s.isEmpty();
    }

    public static boolean isEmpty(Object obj) {
        return null == obj || isEmpty(obj.toString());
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }


    /**
     * @author: zifang
     * @description: Convert an underlined string to camelCase
     * @description: 将带下划线的字符串转大驼峰式
     * @time: 2019-06-13 17:31:00
     * @params: [source] 源字符串
     * @return: java.lang.String 转换后字符串
     */
    public static String underlineToBigCamelCase(String source) {
        if (isEmpty(source)) {
            return source;
        }
        List<String> splitList = listSplit(source, UNDERLINE);
        StringBuilder builder = new StringBuilder();
        for (String s : splitList) {
            builder.append(upperFirst(s.toLowerCase()));
        }
        return builder.toString();
    }

    /**
     * @author: zifang
     * @description: Convert dashed string to camelCase
     * @description: 将带中划线的字符串转大驼峰式
     * @time: 2019-03-24 13:34:00
     * @params: [source] 将字符串中的源字符串
     * @return: java.lang.String 转换后字符串
     */
    public static String strikeToBigCamelCase(String source) {
        if (isEmpty(source)) {
            return source;
        }
        List<String> splitList = listSplit(source, MINUS);
        StringBuilder builder = new StringBuilder();
        for (String s : splitList) {
            builder.append(upperFirst(s.toLowerCase()));
        }
        return builder.toString();
    }


    /**
     * @author: zifang
     * @description: Convert dashed string to camelCase
     * @description: 将带中划线的字符串转小驼峰式
     * @time: 2019-06-13 17:31:00
     * @params: [source] 源字符串
     * @return: java.lang.String 转换后字符串
     */
    public static String strikeToLittleCamelCase(String source) {
        return lowerFirst(strikeToBigCamelCase(source));
    }

    /**
     * @author: zifang
     * @description: Convert dashes to underscores in a string
     * @description: 将字符串中的中划线转下划线
     * @time: 2021-12-13 20:04:00
     * @params: [source] 源字符串
     * @return: java.lang.String 转换后字符串
     */
    public static String strikeToUnderline(String source) {
        if (isEmpty(source)) {
            return source;
        }
        List<String> splitList = listSplit(source, MINUS);
        StringJoiner result = new StringJoiner(UNDERLINE);
        for (String s : splitList) {
            result.add(s);
        }
        return String.valueOf(result);
    }

    /**
     * @author: zifang
     * @description: Uppercase and underline to small camelCase
     * @description: 大写加下划线转小驼峰式
     * @time: 2022-08-23 09:33:30
     * @params: [source] 源字符串
     * @return: java.lang.String 转换后字符串
     */
    public static String underlineUpperToLittleCamelCase(String source) {
        if (isEmpty(source)) {
            return source;
        }
        List<String> splitList = listSplit(source, UNDERLINE);
        StringBuilder builder = new StringBuilder();
        for (String s : splitList) {
            builder.append(upperFirst(s));
        }
        return builder.toString();
    }

    /**
     * @author: zifang
     * @description: Convert underscores to underscores in a string and capitalize
     * @description: 将字符串中的中划线转下划线并大写
     * @time: 2022-08-23 09:18:32
     * @params: [source] 源字符串
     * @return: java.lang.String 转换后字符串
     */
    public static String strikeToUnderlineUpper(String source) {
        if (isEmpty(source)) {
            return source;
        }
        List<String> splitList = listSplit(source, MINUS);
        StringJoiner result = new StringJoiner(UNDERLINE);
        for (String s : splitList) {
            result.add(s.toUpperCase());
        }
        return String.valueOf(result);
    }

    /**
     * @author: zifang
     * @description: Convert an underlined string to camelCase
     * @description: 将带下划线的字符串转小驼峰式
     * @time: 2019-06-13 17:31:00
     * @params: [source] 源字符串
     * @return: java.lang.String 转换后字符串
     */
    public static String underlineToLittleCamelCase(String source) {
        return lowerFirst(underlineToBigCamelCase(source));
    }

    /**
     * @author: zifang
     * @description: Capital letters
     * @description: 首字母大写
     * @time: 2019-06-13 17:31:00
     * @params: [source] 源字符串
     * @return: java.lang.String 转换后字符串
     */
    public static String upperFirst(String source) {
        if (isEmpty(source)) {
            return source;
        }
        char[] chars = source.toCharArray();
        chars[0] = 97 <= chars[0] && chars[0] <= 122 ? (char) (chars[0] - 32) : chars[0];
        return String.valueOf(chars);
    }

    /**
     * @author: zifang
     * @description: First letter lowercase
     * @description: 首字母小写
     * @time: 2019-06-13 17:33:00
     * @params: [source] 源字符串
     * @return: java.lang.String 转换后字符串
     */
    public static String lowerFirst(String source) {
        if (isEmpty(source)) {
            return source;
        }
        char[] chars = source.toCharArray();
        chars[0] = 65 <= chars[0] && chars[0] <= 90 ? (char) (chars[0] + 32) : chars[0];
        return String.valueOf(chars);
    }

    /**
     * @author: zifang
     * @description: Check Email Format
     * @description: 校验邮箱格式
     * @time: 2019-11-02 16:43:00
     * @params: [email] 邮箱
     * @return: boolean 是否符合邮箱格式
     */
    public static boolean validateEmailFormat(String email) {
        if (isEmpty(email)) {
            return false;
        }
        String regex = "^([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String trim(String originStr) {
        return trim(originStr, " ");
    }


    /**
     * @author: zifang
     * @description: Remove the specified string at the beginning and end of the original string (more
     * than one will be removed)
     * @description: 去除原始字符串中开头和结尾的指定字符串(多个都会去除)
     * @time: 2020-09-28 14:46:00
     * @params: [originStr, trim] 源字符串，首尾要去除的空格
     * @return: java.lang.String 转换后字符串
     */
    public static String trim(String originStr, String trim) {
        if (isEmpty(originStr)) {
            return originStr;
        }
        String result = trimStart(originStr, trim);
        return trimEnd(result, trim);
    }

    private static String trimStart(String str, String trim) {
        if (null == str || isEmpty(trim)) {
            return str;
        }
        while (true) {
            if (str.startsWith(trim)) {
                str = str.substring(trim.length());
            } else {
                break;
            }
        }
        return str;
    }

    private static String trimEnd(String str, String trim) {
        if (null == str || isEmpty(trim)) {
            return str;
        }
        while (true) {
            if (str.endsWith(trim)) {
                str = str.substring(0, str.length() - trim.length());
            } else {
                break;
            }
        }
        return str;
    }


    public static List<String> listSplit(String str, String separator) {
        return listSplit(str, separator, true);
    }

    public static String[] split(String str, String separator) {
        List<String> listSplit = listSplit(str, separator);
        String[] strArray = new String[listSplit.size()];
        listSplit.toArray(strArray);
        return strArray;
    }

    /**
     * @author: zifang
     * @description: Split the source string by the specified string
     * @description: 按照指定字符串分割源字符串
     * @time: 2021-05-21 17:04:00
     * @params: [str, delimiter, allowStrEmpty] 字符串，分隔符，空字符是否算一个字符串
     * @return: java.util.List<java.lang.String> 响应参数
     */
    public static List<String> listSplit(String str, String separator, boolean allowStrEmpty) {
        if (str == null) {
            return null;
        } else {
            int len = str.length();
            if (len == 0) {
                return Collections.emptyList();
            } else if (separator != null && !"".equals(separator)) {
                int separatorLength = separator.length();
                List<String> substrings = new ArrayList<>();
                int numberOfSubstrings = 0;
                int beg = 0;
                int end = 0;

                while (end < len) {
                    end = str.indexOf(separator, beg);
                    if (end > -1) {
                        boolean flag = allowStrEmpty ? end >= beg : end > beg;
                        if (flag) {
                            ++numberOfSubstrings;
                            if (numberOfSubstrings == -1) {
                                end = len;
                                substrings.add(str.substring(beg));
                            } else {
                                substrings.add(str.substring(beg, end));
                                beg = end + separatorLength;
                            }
                        } else {

                            beg = end + separatorLength;
                        }
                    } else {
                        substrings.add(str.substring(beg));
                        end = len;
                    }
                }

                return substrings;
            } else {
                {
                    List<String> list = new ArrayList<>();
                    int sizePlus1 = 1;
                    int i = 0;
                    int start = 0;
                    boolean match = false;
                    if (separator != null) {
                        label:
                        while (true) {
                            while (true) {
                                if (i >= len) {
                                    break label;
                                }

                                match = true;
                                ++i;
                            }
                        }
                    } else {
                        label:
                        while (true) {
                            while (true) {
                                if (i >= len) {
                                    break label;
                                }

                                if (Character.isWhitespace(str.charAt(i))) {
                                    if (match) {
                                        if (sizePlus1++ == -1) {
                                            i = len;
                                        }

                                        list.add(str.substring(start, i));
                                        match = false;
                                    }

                                    ++i;
                                    start = i;
                                } else {
                                    match = true;
                                    ++i;
                                }
                            }
                        }
                    }

                    if (match) {
                        list.add(str.substring(start, i));
                    }

                    return list;
                }
            }
        }
    }

    /**
     * @author: zifang
     * @description: 判断字符序列是否为数字
     * @time: 2021-06-07 16:51:00
     * @params: [cs] 请求参数
     * @return: boolean 响应参数
     */
    public static boolean isNumeric(CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        } else {
            int sz = cs.length();

            for (int i = 0; i < sz; ++i) {
                if (!Character.isDigit(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static String parseString(Object object) {
        if (null == object) {
            return null;
        }
        return object.toString();
    }

    public static String parseStringOrDefault(Object object, String defaultValue) {
        if (null == object) {
            return defaultValue;
        }
        return object.toString();
    }

    public static String getOrDefault(String str, String defaultValue) {
        return isNotEmpty(str) ? str : defaultValue;
    }


    public static String parseSteamToString(InputStream inputStream) {
        if (null == inputStream) {
            throw new IllegalArgumentException("inputStream is null");
        }
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines().parallel().collect(Collectors.joining(System.lineSeparator()));
    }

    public static InputStream parseStringToStream(String content, Charset charset) {
        if (isEmpty(content)) {
            throw new IllegalArgumentException("content is empty");
        }
        return new ByteArrayInputStream(content.getBytes(charset));
    }

    public static InputStream parseStringToStream(String content) {
        return parseStringToStream(content, Charset.defaultCharset());
    }

    public static String random(int count) {
        return random(count, true, true);
    }

    public static String random(int count, boolean letters, boolean numbers) {
        return random(count, 0, 0, letters, numbers);
    }

    public static String random(int count, int start, int end, boolean letters, boolean numbers) {
        return random(count, start, end, letters, numbers, null);
    }

    public static String random(int count, int start, int end, final boolean letters,
                                final boolean numbers, final char[] chars) {
        if (count == 0) {
            return "";
        } else if (count < 0) {
            throw new IllegalArgumentException(
                    "Requested random string length " + count + " is less than 0.");
        }
        if (chars != null && chars.length == 0) {
            throw new IllegalArgumentException("The chars array must not be empty");
        }

        if (start == 0 && end == 0) {
            if (chars != null) {
                end = chars.length;
            } else if (!letters && !numbers) {
                end = Character.MAX_CODE_POINT;
            } else {
                end = 'z' + 1;
                start = ' ';
            }
        } else if (end <= start) {
            throw new IllegalArgumentException(
                    "Parameter end (" + end + ") must be greater than start (" + start + ")");
        }

        final int zeroDigitAscii = 48;
        final int firstLetterAscii = 65;

        if (chars == null && (numbers && end <= zeroDigitAscii
                || letters && end <= firstLetterAscii)) {
            throw new IllegalArgumentException(
                    "Parameter end (" + end + ") must be greater then (" + zeroDigitAscii
                            + ") for generating digits " +
                            "or greater then (" + firstLetterAscii + ") for generating letters.");
        }

        final StringBuilder builder = new StringBuilder(count);
        final int gap = end - start;

        while (count-- != 0) {
            final int codePoint;
            if (chars == null) {
                codePoint = ThreadLocalRandom.current().nextInt(gap) + start;

                switch (Character.getType(codePoint)) {
                    case Character.UNASSIGNED:
                    case Character.PRIVATE_USE:
                    case Character.SURROGATE:
                        count++;
                        continue;
                    default:
                }

            } else {
                codePoint = chars[ThreadLocalRandom.current().nextInt(gap) + start];
            }

            final int numberOfChars = Character.charCount(codePoint);
            if (count == 0 && numberOfChars > 1) {
                count++;
                continue;
            }

            if (letters && Character.isLetter(codePoint) || numbers && Character.isDigit(codePoint)
                    || !letters && !numbers) {
                builder.appendCodePoint(codePoint);

                if (numberOfChars == 2) {
                    count--;
                }

            } else {
                count++;
            }
        }
        return builder.toString();
    }

    public static void setIfPresent(String value, Consumer<String> setter) {
        if (isNotEmpty(value)) {
            setter.accept(value);
        }
    }

    public static String leftPad(final String str, final int size) {
        return leftPad(str, size, ' ');
    }


    public static String leftPad(final String str, final int size, final char padChar) {
        if (str == null) {
            return null;
        }
        final int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        if (pads > PAD_LIMIT) {
            return leftPad(str, size, String.valueOf(padChar));
        }
        return repeat(padChar, pads).concat(str);
    }

    public static String leftPad(final String str, final int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = SPACE;
        }
        final int padLen = padStr.length();
        final int strLen = str.length();
        final int pads = size - strLen;
        if (pads <= 0) {
            // returns original String when possible
            return str;
        }
        if (padLen == 1 && pads <= PAD_LIMIT) {
            return leftPad(str, size, padStr.charAt(0));
        }

        if (pads == padLen) {
            return padStr.concat(str);
        } else if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        } else {
            final char[] padding = new char[pads];
            final char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return new String(padding).concat(str);
        }
    }

    /**
     * @author: zifang
     * @description: 超过指定大小取前size位否则右补0
     * @time: 2022-05-05 11:12:27
     * @params: [str, size, padStr] in 入参
     * @return: java.lang.String out 出参
     */
    public static String rightPadWithOver(final String str, final int size, String padStr) {
        if (str.length() > size) {
            return str.substring(0, size);
        }
        return rightPad(str, size, padStr);
    }

    public static String rightPad(final String str, final int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = SPACE;
        }
        final int padLen = padStr.length();
        final int strLen = str.length();
        final int pads = size - strLen;
        if (pads <= 0) {
            return str; // returns original String when possible
        }
        if (padLen == 1 && pads <= PAD_LIMIT) {
            return rightPad(str, size, padStr.charAt(0));
        }

        if (pads == padLen) {
            return str.concat(padStr);
        } else if (pads < padLen) {
            return str.concat(padStr.substring(0, pads));
        } else {
            final char[] padding = new char[pads];
            final char[] padChars = padStr.toCharArray();
            for (int i = 0; i < pads; i++) {
                padding[i] = padChars[i % padLen];
            }
            return str.concat(new String(padding));
        }
    }

    public static String rightPad(final String str, final int size, final char padChar) {
        if (str == null) {
            return null;
        }
        final int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        if (pads > PAD_LIMIT) {
            return rightPad(str, size, String.valueOf(padChar));
        }
        return str.concat(repeat(padChar, pads));
    }

    public static String repeat(final char ch, final int repeat) {
        if (repeat <= 0) {
            return EMPTY;
        }
        final char[] buf = new char[repeat];
        Arrays.fill(buf, ch);
        return new String(buf);
    }

    public static String join(final char delimiter, final String... strings) {
        if (strings.length == 0) {
            return null;
        }
        if (strings.length == 1) {
            return strings[0];
        }
        int length = strings.length - 1;
        for (final String s : strings) {
            if (s == null) {
                continue;
            }
            length += s.length();
        }
        final StringBuilder sb = new StringBuilder(length);
        if (strings[0] != null) {
            sb.append(strings[0]);
        }
        for (int i = 1; i < strings.length; ++i) {
            if (!isEmpty(strings[i])) {
                sb.append(delimiter).append(strings[i]);
            } else {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public static String replace(final String text, final String searchString,
                                 final String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    public static String replace(final String text, final String searchString,
                                 final String replacement, final int max) {
        return replace(text, searchString, replacement, max, false);
    }

    private static String replace(final String text, String searchString, final String replacement,
                                  int max, final boolean ignoreCase) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        if (ignoreCase) {
            searchString = searchString.toLowerCase();
        }
        int start = 0;
        int end = ignoreCase ? indexOfIgnoreCase(text, searchString, start)
                : indexOf(text, searchString, start);
        if (end == INDEX_NOT_FOUND) {
            return text;
        }
        final int replLength = searchString.length();
        int increase = Math.max(replacement.length() - replLength, 0);
        increase *= max < 0 ? 16 : Math.min(max, 64);
        final StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != INDEX_NOT_FOUND) {
            buf.append(text, start, end).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = ignoreCase ? indexOfIgnoreCase(text, searchString, start)
                    : indexOf(text, searchString, start);
        }
        buf.append(text, start, text.length());
        return buf.toString();
    }

    public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr) {
        return indexOfIgnoreCase(str, searchStr, 0);
    }

    public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr,
                                        int startPos) {
        if (str == null || searchStr == null) {
            return INDEX_NOT_FOUND;
        }
        if (startPos < 0) {
            startPos = 0;
        }
        final int endLimit = str.length() - searchStr.length() + 1;
        if (startPos > endLimit) {
            return INDEX_NOT_FOUND;
        }
        if (searchStr.length() == 0) {
            return startPos;
        }
        for (int i = startPos; i < endLimit; i++) {
            if (regionMatches(str, i, searchStr, searchStr.length())) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static int indexOf(final CharSequence sequence, final CharSequence searchSequence,
                              final int startPos) {
        if (sequence == null || searchSequence == null) {
            return INDEX_NOT_FOUND;
        }
        if (sequence instanceof String) {
            return ((String) sequence).indexOf(searchSequence.toString(), startPos);
        } else if (sequence instanceof StringBuilder) {
            return ((StringBuilder) sequence).indexOf(searchSequence.toString(), startPos);
        } else if (sequence instanceof StringBuffer) {
            return ((StringBuffer) sequence).indexOf(searchSequence.toString(), startPos);
        }
        return sequence.toString().indexOf(searchSequence.toString(), startPos);
    }

    private static boolean regionMatches(final CharSequence cs,
                                         final int thisStart, final CharSequence substring, final int length) {
        if (cs instanceof String && substring instanceof String) {
            return ((String) cs).regionMatches(true, thisStart, (String) substring, 0, length);
        }
        int index1 = thisStart;
        int index2 = 0;
        int tmpLen = length;

        final int srcLen = cs.length() - thisStart;
        final int otherLen = substring.length();

        if ((thisStart < 0) || (length < 0)) {
            return false;
        }

        if (srcLen < length || otherLen < length) {
            return false;
        }

        while (tmpLen-- > 0) {
            final char c1 = cs.charAt(index1++);
            final char c2 = substring.charAt(index2++);

            if (c1 == c2) {
                continue;
            }

            final char u1 = Character.toUpperCase(c1);
            final char u2 = Character.toUpperCase(c2);
            if (u1 != u2 && Character.toLowerCase(u1) != Character.toLowerCase(u2)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isFormat(String message) {
        Matcher matcher = FORMAT_PATTERN.matcher(message);
        return matcher.find();
    }

    public static StringReader getReader(CharSequence str) {
        if (null == str) {
            return null;
        }
        return new StringReader(str.toString());
    }

    public static String str(CharSequence cs) {
        return null == cs ? null : cs.toString();
    }


    public static int atoi(String str) {
        long result = 0;

        boolean start = false;
        boolean plusOrMinus = true;
        for (int i = 0; i < str.length(); i++) {
            char currentChar = str.charAt(i);

            if (Character.isDigit(currentChar)) {
                int numericValue = Character.getNumericValue(currentChar);
                if (plusOrMinus) {
                    result = result * 10 + numericValue;
                    if (result > Integer.MAX_VALUE) {
                        return Integer.MAX_VALUE;
                    }
                } else {
                    result = result * 10 - numericValue;
                    if (result < Integer.MIN_VALUE) {
                        return Integer.MIN_VALUE;
                    }
                }

                start = true;
            } else if (start) {
                break;
            } else if (MINUS_CHAR == currentChar) {
                plusOrMinus = false;
                start = true;
            } else if (PLUS_CHAR == currentChar) {
                start = true;
            } else if (SPACE_CHAR == currentChar) {
            } else {
                break;
            }
        }
        return (int) result;
    }

    public static boolean hasText(String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }


    /**
     * @author: zifang
     * @description: The character sequence contains the specified character sequence
     * @description: 字符序列包含指定字符序列
     * @time: 2022-06-07 16:27:01
     * @params: [seq, searchSeq] 字符序列，被包含字符序列
     * @return: boolean 是否包含
     */
    public static boolean contains(final CharSequence seq, final CharSequence searchSeq) {
        if (seq == null || searchSeq == null) {
            return false;
        }
        return indexOf(seq, searchSeq, 0) >= 0;
    }


    /**
     * @author: zifang
     * @description: The character sequence contains the specified character
     * @description: 字符序列包含指定字符
     * @time: 2022-06-07 16:29:41
     * @params: [seq, searchChar] 字符序列，被包含字符
     * @return: boolean 是否包含
     */
    public static boolean contains(final CharSequence seq, final int searchChar) {
        if (isEmpty(seq)) {
            return false;
        }
        return indexOf(seq, searchChar, 0) >= 0;
    }

    /**
     * @author: zifang
     * @description: Retrieves the specified character starting from the specified subscript in the
     * string
     * @description: 在字符串中从指定下标开始检索指定字符
     * @time: 2022-06-07 16:06:50
     * @params: [sequence, searchChar, startPos] 字符串，检索字符，起始下标
     * @return: int 下标
     */
    public static int indexOf(final CharSequence cs, final int searchChar, int start) {
        if (cs instanceof String) {
            return ((String) cs).indexOf(searchChar, start);
        }
        final int sz = cs.length();
        if (start < 0) {
            start = 0;
        }
        if (searchChar < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
            for (int i = start; i < sz; i++) {
                if (cs.charAt(i) == searchChar) {
                    return i;
                }
            }
            return INDEX_NOT_FOUND;
        }
        if (searchChar <= Character.MAX_CODE_POINT) {
            final char[] chars = Character.toChars(searchChar);
            for (int i = start; i < sz - 1; i++) {
                final char high = cs.charAt(i);
                final char low = cs.charAt(i + 1);
                if (high == chars[0] && low == chars[1]) {
                    return i;
                }
            }
        }
        return INDEX_NOT_FOUND;
    }

//    /**
//     * @author: zifang
//     * @description: Get a random chinese character
//     * @description: 随机获取一个汉字
//     * @time: 2022-09-12 19:47:24
//     * @params: []
//     * @return: char random chinese character 随机汉字
//     */
//    public static char getRandomChineseCharacter() {
//        return (char) (CHINESE_CHARACTER_START + ThreadLocalRandom.current()
//                .nextInt(CHINESE_CHARACTER_LENGTH));
//    }

}
