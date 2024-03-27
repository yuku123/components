package com.zifang.util.core.lang;

import com.zifang.util.core.constant.Ascii;
import com.zifang.util.core.constant.HtmlEntities;
import com.zifang.util.core.lang.regex.Patterns;
import com.zifang.util.core.lang.validator.Validator;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

public class StringUtil {

    private static final Predicate<String> NULL_STRING_PREDICATE = Objects::isNull;
    private static final Supplier<String> NULL_STRING_MSG_SUPPLIER = () -> "'value' should be not null.";
    private static final String[] EMPTY_ARRAY = new String[0];
    private static final char SEPARATOR = '_';


    public static boolean isBlank(String str) {

        int length;

        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            if (!CharUtil.isBlankChar(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断字符串是否全部都为小写
     */
    public static boolean isAllLowerCase(String value) {
        if (value == null || "".equals(value)) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isLowerCase(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否全部大写
     */
    public static boolean isAllUpperCase(String value) {
        if (value == null || "".equals(value)) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isUpperCase(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * 判断字符串是否不为空
     */
    public static boolean isNotEmptyString(String string) {
        return string != null && string.length() > 0;
    }

    /**
     * 文本追加
     */
    public static String append(String value, String... appends) {

        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);

        if (appends == null || appends.length == 0) {
            return value;
        } else {
            StringJoiner joiner = new StringJoiner("");
            Arrays.stream(appends).forEach(joiner::add);
            return value + joiner;
        }
    }

    /**
     * 文本内存在多个连续空格场合，缩进为单个空格
     */
    public static String collapseWhitespace(String value) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return value.trim().replaceAll("\\s\\s+", " ");
    }

    public static boolean contains(String value, String needle) {
        return contains(value, needle, false);
    }

    public static boolean contains(String value, String needle, boolean caseSensitive) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (caseSensitive) {
            return value.contains(needle);
        }
        return value.toLowerCase().contains(needle.toLowerCase());
    }

    public static boolean containsAll(String value, String[] needles) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return Arrays.stream(needles).allMatch(needle -> contains(value, needle, false));
    }

    public static boolean containsAll(String value, String[] needles, boolean caseSensitive) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return Arrays.stream(needles).allMatch(needle -> contains(value, needle, caseSensitive));
    }

    public static boolean containsAny(String value, String[] needles) {
        return containsAny(value, needles, false);
    }

    public static boolean containsAny(String value, String[] needles, boolean caseSensitive) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return Arrays.stream(needles).anyMatch(needle -> contains(value, needle, caseSensitive));
    }

    /**
     * 统计子串出现次数
     */
    public static long countSubstr(String value, String subStr) {
        return countSubstr(value, subStr, true, false);
    }

    public static long countSubstr(String value, String subStr, boolean caseSensitive, boolean allowOverlapping) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return countSubstr(caseSensitive ? value : value.toLowerCase(), caseSensitive ? subStr : subStr.toLowerCase(),
                allowOverlapping, 0L);
    }

    public static boolean endsWith(String value, String search) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return endsWith(value, search, value.length(), true);
    }

    public static boolean endsWith(String value, String search, boolean caseSensitive) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return endsWith(value, search, value.length(), caseSensitive);
    }

    public static boolean endsWith(String value, String search, int position,
                                   boolean caseSensitive) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        int remainingLength = position - search.length();
        if (caseSensitive) {
            return value.indexOf(search, remainingLength) > -1;
        }
        return value.toLowerCase().indexOf(search.toLowerCase(), remainingLength) > -1;
    }

    public static String ensureLeft(String value, String prefix) {
        return ensureLeft(value, prefix, true);
    }

    public static String ensureLeft(String value, String prefix, boolean caseSensitive) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (caseSensitive) {
            return value.startsWith(prefix) ? value : prefix + value;
        }
        String _value = value.toLowerCase();
        String _prefix = prefix.toLowerCase();
        return _value.startsWith(_prefix) ? value : prefix + value;
    }

    public static String ensureRight(String value, String suffix) {
        return ensureRight(value, suffix, true);
    }

    public static String ensureRight(String value, String suffix, boolean caseSensitive) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return endsWith(value, suffix, caseSensitive) ? value : append(value, suffix);
    }

    public static String base64Decode(String value) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
    }

    public static String base64Encode(String value) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    public static String binDecode(String value) {
        return decode(value, 16, 2);
    }

    public static String binEncode(String value) {
        return encode(value, 16, 2);
    }

    public static String decDecode(String value) {
        return decode(value, 5, 10);
    }

    public static String decEncode(String value) {
        return encode(value, 5, 10);
    }

    public static String decode(String value, int digits, int radix) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return Arrays.stream(value.split("(?<=\\G.{" + digits + "})"))
                .map(data -> String.valueOf(Character.toChars(Integer.parseInt(data, radix))))
                .collect(joining());
    }

    public static String encode(String value, int digits, int radix) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return value.chars().mapToObj(ch -> leftPad(Integer.toString(ch, radix), "0", digits)).collect(joining());
    }

    /**
     * Returns the first n chars of String
     *
     * @param value The input String
     * @param n     Number of chars to return
     * @return The first n chars
     */
    public static Optional<String> first(String value, int n) {
        return Optional.ofNullable(value).filter(v -> !v.isEmpty()).map(v -> v.substring(0, n));
    }

    /**
     * Return the first char of String
     *
     * @param value The input String
     * @return The first char
     */
    public static Optional<String> head(String value) {
        return first(value, 1);
    }

    /**
     * Formats a string using parameters
     *
     * @param value  The value to be formatted
     * @param params Parameters to be described in the string
     * @return The formatted string
     */
    public static String format(String value, String... params) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        Pattern p = Pattern.compile("\\{(\\w+)}");
        Matcher m = p.matcher(value);
        String result = value;
        while (m.find()) {
            int paramNumber = Integer.parseInt(m.group(1));
            if (params == null || paramNumber >= params.length) {
                throw new IllegalArgumentException("params does not have value for " + m.group());
            }
            result = result.replace(m.group(), params[paramNumber]);
        }
        return result;
    }

    /**
     * Convert hexadecimal unicode (4 digits) string to string chars
     *
     * @param value The value to decode
     * @return The decoded String
     */
    public static String hexDecode(String value) {
        return decode(value, 4, 16);
    }

    /**
     * Convert string chars to hexadecimal unicode (4 digits)
     *
     * @param value The value to encode
     * @return String in hexadecimal format.
     */
    public static String hexEncode(String value) {
        return encode(value, 4, 16);
    }

    /**
     * The indexOf() method returns the index within the calling String of the first occurrence of the specified value, starting the search at fromIndex.
     * Returns -1 if the value is not found.
     *
     * @param value         The input String
     * @param needle        The search String
     * @param offset        The offset to start searching from.
     * @param caseSensitive boolean to indicate whether search should be case sensitive
     * @return Returns position of first occurrence of needle.
     */
    public static int indexOf(String value, String needle, int offset, boolean caseSensitive) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (caseSensitive) {
            return value.indexOf(needle, offset);
        }
        return value.toLowerCase().indexOf(needle.toLowerCase(), offset);
    }

    /**
     * Tests if two Strings are inequal
     *
     * @param first  The first String
     * @param second The second String
     * @return true if first and second are not equal false otherwise
     */
    public static boolean unequal(String first, String second) {
        return !Objects.equals(first, second);
    }

    /**
     * Tests if two Strings are inequal
     *
     * @param first  The first String
     * @param second The second String
     * @return true if first and second are not equal false otherwise
     * @deprecated use unequal instead
     */
    public static boolean inequal(String first, String second) {
        return !Objects.equals(first, second);
    }

    /**
     * Inserts 'substr' into the 'value' at the 'index' provided.
     *
     * @param value  The input String
     * @param substr The String to insert
     * @param index  The index to insert substr
     * @return String with substr added
     */
    public static String insert(String value, String substr, int index) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        Validator.validate(substr, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (index > value.length()) {
            return value;
        }
        return append(value.substring(0, index), substr, value.substring(index));
    }

    /**
     * Verifies if String is uppercase
     *
     * @param value The input String
     * @return true if String is uppercase false otherwise
     */
    public static boolean isUpperCase(String value) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        for (int i = 0; i < value.length(); i++) {
            if (Character.isLowerCase(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifies if String is lower case
     *
     * @param value The input String
     * @return true if String is lowercase false otherwise
     */
    public static boolean isLowerCase(String value) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        for (int i = 0; i < value.length(); i++) {
            if (Character.isUpperCase(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return the last n chars of String
     *
     * @param value The input String
     * @param n     Number of chars to return
     * @return n Last characters
     */
    public static String last(String value, int n) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (n > value.length()) {
            return value;
        }
        return value.substring(value.length() - n);
    }

    /**
     * Returns a new string of a given length such that the beginning of the string is padded.
     *
     * @param value  The input String
     * @param pad    The pad
     * @param length Length of the String we want
     * @return Padded String
     */
    public static String leftPad(String value, String pad, int length) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        Validator.validate(pad, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (value.length() > length) {
            return value;
        }
        return append(repeat(pad, length - value.length()), value);
    }

    /**
     * Checks whether Object is String
     *
     * @param value The input String
     * @return true if Object is a String false otherwise
     */
    public static boolean isString(Object value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("value can't be null");
        }
        return value instanceof String;
    }

    /**
     * This method returns the index within the calling String object of the last occurrence of the specified value, searching backwards from the offset.
     * Returns -1 if the value is not found. The search starts from the end and case sensitive.
     *
     * @param value  The input String
     * @param needle The search String
     * @return Return position of the last occurrence of 'needle'.
     */
    public static int lastIndexOf(String value, String needle) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return lastIndexOf(value, needle, value.length(), true);
    }

    /**
     * This method returns the index within the calling String object of the last occurrence of the specified value, searching backwards from the offset.
     * Returns -1 if the value is not found. The search starts from the end and case sensitive.
     *
     * @param value         The input String
     * @param needle        The search String
     * @param caseSensitive true or false
     * @return Return position of the last occurrence of 'needle'.
     */
    public static int lastIndexOf(String value, String needle, boolean caseSensitive) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return lastIndexOf(value, needle, value.length(), caseSensitive);
    }

    /**
     * This method returns the index within the calling String object of the last occurrence of the specified value, searching backwards from the offset.
     * Returns -1 if the value is not found.
     *
     * @param value         The input String
     * @param needle        The search String
     * @param offset        The index to start search from
     * @param caseSensitive whether search should be case sensitive
     * @return Return position of the last occurrence of 'needle'.
     */
    public static int lastIndexOf(String value, String needle, int offset,
                                  boolean caseSensitive) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        Validator.validate(needle, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (caseSensitive) {
            return value.lastIndexOf(needle, offset);
        }
        return value.toLowerCase().lastIndexOf(needle.toLowerCase(), offset);
    }

    /**
     * Removes all spaces on left
     *
     * @param value The input String
     * @return String without left border spaces
     */
    public static String leftTrim(String value) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return value.replaceAll("^\\s+", "");
    }

    /**
     * Returns length of String. Delegates to java.lang.String length method.
     *
     * @param value The input String
     * @return Length of the String
     */
    public static int length(String value) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return value.length();
    }

    /**
     * Return a new String starting with prepends
     *
     * @param value    The input String
     * @param prepends Strings to prepend
     * @return The prepended String
     */
    public static String prepend(String value, String... prepends) {
        return prependArray(value, prepends);
    }

    /**
     * Return a new String starting with prepends
     *
     * @param value    The input String
     * @param prepends Strings to prepend
     * @return The prepended String
     */
    public static String prependArray(String value, String[] prepends) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (prepends == null || prepends.length == 0) {
            return value;
        }
        StringJoiner joiner = new StringJoiner("");
        for (String prepend : prepends) {
            joiner.add(prepend);
        }
        return joiner.toString() + value;
    }


    /**
     * Returns a new String with the prefix removed, if present. This is case sensitive.
     *
     * @param value  The input String
     * @param prefix String to remove on left
     * @return The String without prefix
     */
    public static String removeLeft(String value, String prefix) {
        return removeLeft(value, prefix, true);
    }

    /**
     * Returns a new String with the prefix removed, if present.
     *
     * @param value         The input String
     * @param prefix        String to remove on left
     * @param caseSensitive ensure case sensitivity
     * @return The String without prefix
     */
    public static String removeLeft(String value, String prefix, boolean caseSensitive) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        Validator.validate(prefix, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (caseSensitive) {
            return value.startsWith(prefix) ? value.substring(prefix.length()) : value;
        }
        return value.toLowerCase().startsWith(prefix.toLowerCase()) ? value.substring(prefix.length()) : value;
    }

    /**
     * Remove all non word characters.
     *
     * @param value The input String
     * @return String without non-word characters
     */
    public static String removeNonWords(String value) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return value.replaceAll("[^\\w]+", "");
    }

    /**
     * Returns a new string with the 'suffix' removed, if present. Search is case sensitive.
     *
     * @param value  The input String
     * @param suffix The suffix to remove
     * @return The String without suffix!
     */
    public static String removeRight(String value, String suffix) {
        return removeRight(value, suffix, true);
    }

    /**
     * Returns a new string with the 'suffix' removed, if present.
     *
     * @param value         The input String
     * @param suffix        The suffix to remove
     * @param caseSensitive whether search should be case sensitive or not
     * @return The String without suffix!
     */
    public static String removeRight(String value, String suffix, boolean caseSensitive) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        Validator.validate(suffix, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return endsWith(value, suffix, caseSensitive) ? value
                .substring(0, value.toLowerCase().lastIndexOf(suffix.toLowerCase())) : value;
    }

    /**
     * Remove all spaces and replace for value.
     *
     * @param value The input String
     * @return String without spaces
     */
    public static String removeSpaces(String value) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return value.replaceAll("\\s", "");
    }

    /**
     * Returns a repeated string given a multiplier.
     *
     * @param value      The input String
     * @param multiplier Number of repeats
     * @return The String repeated
     */
    public static String repeat(String value, int multiplier) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return Stream.generate(() -> value).limit(multiplier).collect(joining());
    }

    /**
     * Replace all occurrences of 'search' value to 'newvalue'. Uses String replace method.
     *
     * @param value         The input
     * @param search        The String to search
     * @param newValue      The String to replace
     * @param caseSensitive whether search should be case sensitive or not
     * @return String replaced with 'newvalue'.
     */
    public static String replace(String value, String search, String newValue, boolean caseSensitive) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        Validator.validate(search, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (caseSensitive) {
            return value.replace(search, newValue);
        }
        return Pattern.compile(search, Pattern.CASE_INSENSITIVE).matcher(value)
                .replaceAll(Matcher.quoteReplacement(newValue));
    }


    /**
     * Returns a new string of a given length such that the ending of the string is padded.
     *
     * @param value  The input String
     * @param length Max length of String.
     * @param pad    Character to repeat
     * @return Right padded String
     */
    public static String rightPad(String value, String pad, int length) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (value.length() > length) {
            return value;
        }
        return append(value, repeat(pad, length - value.length()));
    }

    /**
     * Remove all spaces on right.
     *
     * @param value The String
     * @return String without right boarders spaces.
     */
    public static String rightTrim(String value) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return value.replaceAll("\\s+$", "");
    }

    /**
     * Truncate the string securely, not cutting a word in half. It always returns the last full word.
     *
     * @param value  The input String
     * @param length Max size of the truncated String
     * @param filler String that will be added to the end of the return string. Example: '...'
     * @return The truncated String
     */
    public static String safeTruncate(String value, int length, String filler) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (length == 0) {
            return "";
        }
        if (length >= value.length()) {
            return value;
        }

        String[] words = words(value);
        StringJoiner result = new StringJoiner(" ");
        int spaceCount = 0;
        for (String word : words) {
            if (result.length() + word.length() + filler.length() + spaceCount > length) {
                break;
            } else {
                result.add(word);
                spaceCount++;
            }
        }
        return append(result.toString(), filler);
    }

    /**
     * Splits a String to words
     *
     * @param value The input String
     * @return Words Array
     */
    public static String[] words(String value) {
        return words(value, "\\s+");
    }

    /**
     * Splits a String to words by delimiter, \s+ by default
     *
     * @param value     The input String
     * @param delimiter delimiter for splitting input String
     * @return words array
     */
    public static String[] words(String value, String delimiter) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return value.split(delimiter);
    }


    /**
     * Truncate the unsecured form string, cutting the independent string of required position.
     *
     * @param value  Value will be truncated unsecurely.
     * @param length Size of the returned string.
     * @param filler Value that will be added to the end of the return string. Example: '...'
     * @return String truncated unsafely.
     */
    public static String truncate(String value, int length, String filler) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        if (length == 0) {
            return "";
        }
        if (length >= value.length()) {
            return value;
        }
        return append(value.substring(0, length - filler.length()), filler);
    }

    /**
     * Converts all HTML entities to applicable characters.
     *
     * @param encodedHtml The encoded HTML
     * @return The decoded HTML
     */
    public static String htmlDecode(String encodedHtml) {
        Validator.validate(encodedHtml, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        String[] entities = encodedHtml.split("&\\W+;");
        return Arrays.stream(entities).map(e -> HtmlEntities.decodedEntities.get(e)).collect(joining());
    }

    /**
     * Convert all applicable characters to HTML entities.
     *
     * @param html The HTML to encode
     * @return The encoded data
     */
    public static String htmlEncode(String html) {
        Validator.validate(html, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return html.chars().mapToObj(c -> "\\u" + String.format("%04x", c).toUpperCase())
                .map(HtmlEntities.encodedEntities::get).collect(joining());
    }

    /**
     * 乱排string
     * */

    /**
     * Alias of substring method
     *
     * @param value The input String
     * @param begin Start of slice.
     * @param end   End of slice.
     * @return The String sliced!
     */
    public static String slice(String value, int begin, int end) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return value.substring(begin, end);
    }

    /**
     * Convert a String to a slug
     *
     * @param value The value to slugify
     * @return The slugified value
     */
    public static String slugify(String value) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        String transliterated = transliterate(collapseWhitespace(value.trim().toLowerCase()));
        return Arrays.stream(words(transliterated.replace("&", "-and-"), "\\W+")).collect(joining("-"));
    }

    /**
     * Remove all non valid characters.
     *
     * @param value The input String
     * @return String without non valid characters.
     */
    public static String transliterate(String value) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        String result = value;
        Set<Map.Entry<String, List<String>>> entries = Ascii.ascii.entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            for (String ch : entry.getValue()) {
                result = result.replace(ch, entry.getKey());
            }
        }
        return result;
    }

    /**
     * Surrounds a 'value' with the given 'prefix' and 'suffix'.
     *
     * @param value  The input String
     * @param prefix prefix. If suffix is null then prefix is used
     * @param suffix suffix
     * @return The String with surround substrs!
     */
    public static String surround(String value, String prefix, String suffix) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        String _prefix = Optional.ofNullable(prefix).orElse("");
        return append(_prefix, value, Optional.ofNullable(suffix).orElse(_prefix));
    }

    /**
     * Transform to camelCase
     *
     * @param value The input String
     * @return String in camelCase.
     */
    public static String toCamelCase(String value) {
        if (value == null || value.length() == 0) {
            return "";
        }
        String str = toStudlyCase(value);
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * Transform to StudlyCaps.
     *
     * @param value The input String
     * @return String in StudlyCaps.
     */
    public static String toStudlyCase(String value) {
        Validator.validate(value, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        String[] words = collapseWhitespace(value.trim()).split("\\s*(_|-|\\s)\\s*");
        return Arrays.stream(words).filter(w -> !w.trim().isEmpty()).map(StringUtil::upperFirst).collect(joining());
    }

    /**
     * Return tail of the String
     *
     * @param value The input String
     * @return String tail
     */
    public static Optional<String> tail(String value) {
        return Optional.ofNullable(value).filter(v -> !v.isEmpty()).map(v -> last(v, v.length() - 1));
    }

    /**
     * Decamelize String
     *
     * @param value The input String
     * @param chr   string to use
     * @return String decamelized.
     */
    public static String toDecamelize(String value, String chr) {
        String camelCasedString = toCamelCase(value);
        String[] words = camelCasedString.split("(?=\\p{Upper})");
        return Arrays.stream(words).map(String::toLowerCase).collect(joining(Optional.ofNullable(chr).orElse(" ")));
    }

    /**
     * Transform to kebab-case.
     *
     * @param value The input String
     * @return String in kebab-case.
     */
    public static String toKebabCase(String value) {
        return toDecamelize(value, "-");
    }

    /**
     * Transform to snake_case.
     *
     * @param value The input String
     * @return String in snake_case.
     */
    public static String toSnakeCase(String value) {
        return toDecamelize(value, "_");
    }


    /**
     * Converts the first character of string to upper case and the remaining to lower case.
     *
     * @param input The string to capitalize
     * @return The capitalized string
     */
    public static String capitalize(String input) throws IllegalArgumentException {
        if (input == null) {
            throw new IllegalArgumentException("input can't be null");
        }
        if (input.length() == 0) {
            return "";
        }
        return head(input).map(String::toUpperCase).map(h -> tail(input).map(t -> h + t.toLowerCase()).orElse(h)).get();
    }

    /**
     * Converts the first character of string to lower case.
     *
     * @param input The string to convert
     * @return The converted string
     */
    public static String lowerFirst(String input) throws IllegalArgumentException {
        if (input == null) {
            throw new IllegalArgumentException("input can't be null");
        }
        if (input.length() == 0) {
            return "";
        }

        return head(input).map(String::toLowerCase).map(h -> tail(input).map(t -> h + t).orElse(h)).get();
    }

    /**
     * Verifies whether String is enclosed by encloser
     *
     * @param input    The input String
     * @param encloser String which encloses input String
     * @return true if enclosed false otherwise
     */
    public static boolean isEnclosedBetween(String input, String encloser) {
        return isEnclosedBetween(input, encloser, encloser);
    }

    /**
     * Verifies whether String is enclosed by encloser
     *
     * @param input         The input String
     * @param leftEncloser  String which encloses input String at left start
     * @param rightEncloser String which encloses input String at the right end
     * @return true if enclosed false otherwise
     */
    public static boolean isEnclosedBetween(String input, String leftEncloser, String rightEncloser) {
        if (input == null) {
            throw new IllegalArgumentException("input can't be null");
        }
        if (leftEncloser == null) {
            throw new IllegalArgumentException("leftEncloser can't be null");
        }
        if (rightEncloser == null) {
            throw new IllegalArgumentException("rightEncloser can't be null");
        }
        return input.startsWith(leftEncloser) && input.endsWith(rightEncloser);
    }

    /**
     * Converts the first character of string to upper case.
     *
     * @param input The string to convert.
     * @return Returns the converted string.
     */
    public static String upperFirst(String input) {
        if (input == null) {
            throw new IllegalArgumentException("input can't be null");
        }
        return head(input).map(String::toUpperCase).map(h -> tail(input).map(t -> h + t).orElse(h)).get();
    }

    /**
     * Removes leading characters from string.
     *
     * @param input The string to trim.
     * @param chars The characters to trim.
     * @return Returns the trimmed string.
     */
    public static Optional<String> trimStart(String input, String... chars) {
        return Optional.ofNullable(input).filter(v -> !v.isEmpty()).map(v -> {
            String pattern = String.format("^[%s]+", String.join("\\", Arrays.asList(chars)));
            return v.replaceAll(pattern, "");
        });
    }

    /**
     * 左边去除空字符串
     * 右边去除空字符串
     */


    /**
     * 统计一个字符串内的char:个数
     *
     * @param input The input string
     * @return A map containing the number of occurrences of each character in the string
     */
    public static Map<Character, Long> charsCount(String input) {
        if (isNullOrEmpty(input)) {
            return Collections.emptyMap();
        }
        return input.chars().mapToObj(c -> (char) c).collect(groupingBy(identity(), counting()));
    }

    /**
     * Changes passed in string to all lower case and adds underscore between words.
     *
     * @param input The input string
     * @return the input string in all lower case with underscores between words
     */
    public static String underscored(String input) {
        if (input == null || input.length() == 0) {
            return "";
        }

        return input.trim().replaceAll("([a-z\\d])([A-Z]+)", "$1_$2").replaceAll("[-\\s]+", "_").toLowerCase();
    }

    /**
     * 将有换行符的字符串 转化为list
     *
     * @param input 被转化的字符串
     * @return 以换行为单元切分的list
     */
    public static String[] lines(String input) {
        if (input == null) {
            return EMPTY_ARRAY;
        }
        return input.split("\r\n?|\n");
    }

    /**
     * Converts a underscored or camelized string into an dasherized one.
     *
     * @param input The input String
     * @return dasherized String.
     */
    public static String dasherize(String input) {
        return toKebabCase(input);
    }

    /**
     * Converts an underscored, camelized, or dasherized string into a humanized one. Also removes beginning and ending whitespace.
     *
     * @param input The input String
     * @return humanized version of String
     */
    public static String humanize(String input) {
        if (input == null || input.length() == 0) {
            return "";
        }
        return upperFirst(underscored(input).replaceAll("_", " "));
    }

    /**
     * Returns a copy of the string in which all the case-based characters have had their case swapped.
     *
     * @param input Input string
     * @return String with all the case swapped
     */
    public static String swapCase(String input) {
        if (input == null || input.length() == 0) {
            return "";
        }
        StringBuilder resultBuilder = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                resultBuilder.append(Character.toLowerCase(ch));
            } else {
                resultBuilder.append(Character.toUpperCase(ch));
            }
        }
        return resultBuilder.toString();
    }

    /**
     * Returns a string representation of the number passed in where groups of three digits are delimited by comma
     *
     * @param number Input number
     * @return formatted String
     */
    public static String formatNumber(long number) {
        String stringRepresentation = Long.toString(number);
        StringBuilder sb = new StringBuilder();
        int bound = stringRepresentation.length() - 1;
        String delimiter = ",";
        int counter = 0;
        for (int i = bound; i >= 0; i--) {
            char c = stringRepresentation.charAt(i);
            if (i != bound && counter % 3 == 0) {
                sb.append(delimiter);
            }
            sb.append(c);
            counter++;
        }
        return sb.reverse().toString();
    }

    public static String[] chop(String input, int step) {
        if (input == null || input.length() == 0) {
            return EMPTY_ARRAY;
        }
        if (step == 0) {
            return new String[]{input};
        }
        int strLength = input.length();
        int iterations = strLength % step == 0 ? strLength / step : strLength / step + 1;
        return IntStream.iterate(0, i -> i + step)
                .limit(iterations)
                .mapToObj(i -> input.substring(i, (i + step) < strLength ? i + step : strLength))
                .toArray(String[]::new);
    }

    /**
     * Converts a String into its Start Case version
     * https://en.wikipedia.org/wiki/Letter_case#Stylistic_or_specialised_usage
     *
     * @param input The input String
     * @return Start Case String
     */
    public static String startCase(String input) {
        Validator.validate(input, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        // split into a word when we encounter a space, or an underscore, or a dash, or a switch from lower to upper case
        String[] words = words(input, "\\s|_|-|(?<=[a-z])(?=[A-Z])");
        return Arrays.stream(words).filter(w -> !w.trim().isEmpty())
                .map(w -> upperFirst(w.toLowerCase())).collect(joining(" "));
    }

    public static String escapeRegExp(String input) {
        Validator.validate(input, NULL_STRING_PREDICATE, NULL_STRING_MSG_SUPPLIER);
        return input.replaceAll("[\\\\\\^\\$\\*\\+\\-\\?\\.\\|\\(\\)\\{\\}\\[\\]]", "\\\\$0");
    }


    private static long countSubstr(String value, String subStr, boolean allowOverlapping, long count) {
        int position = value.indexOf(subStr);
        if (position == -1) {
            return count;
        }
        int offset;
        if (!allowOverlapping) {
            offset = position + subStr.length();
        } else {
            offset = position + 1;
        }
        return countSubstr(value.substring(offset), subStr, allowOverlapping, ++count);
    }

    private static boolean isNullOrEmpty(String input) {
        return input == null || input.isEmpty();
    }


    public static String rightPad(String str, int size, char padChar) {
        if (str.length() < size) {
            String padding = repeat(padChar, size - str.length());
            return str + padding;
        }
        return str;
    }

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

                for (int i = 0; i < pads; ++i) {
                    padding[i] = padChars[i % padLen];
                }

                return (new String(padding)).concat(str);
            }
        }
    }


    public static String repeat(char ch, int repeat) {
        if (repeat <= 0) {
            return "";
        } else {
            char[] buf = new char[repeat];

            for (int i = repeat - 1; i >= 0; --i) {
                buf[i] = ch;
            }

            return new String(buf);
        }
    }

    /**
     * 将半角的符号转换成全角符号.(即英文字符转中文字符)
     *
     * @param str 要转换的字符
     * @return
     */
    public static String changeToFull(String str) {
        String source = "1234567890!@#$%^&*()abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_=+\\|[];:'\",<.>/?";
        String[] decode = {"１", "２", "３", "４", "５", "６", "７", "８", "９", "０",
                "！", "＠", "＃", "＄", "％", "︿", "＆", "＊", "（", "）", "ａ", "ｂ",
                "ｃ", "ｄ", "ｅ", "ｆ", "ｇ", "ｈ", "ｉ", "ｊ", "ｋ", "ｌ", "ｍ", "ｎ",
                "ｏ", "ｐ", "ｑ", "ｒ", "ｓ", "ｔ", "ｕ", "ｖ", "ｗ", "ｘ", "ｙ", "ｚ",
                "Ａ", "Ｂ", "Ｃ", "Ｄ", "Ｅ", "Ｆ", "Ｇ", "Ｈ", "Ｉ", "Ｊ", "Ｋ", "Ｌ",
                "Ｍ", "Ｎ", "Ｏ", "Ｐ", "Ｑ", "Ｒ", "Ｓ", "Ｔ", "Ｕ", "Ｖ", "Ｗ", "Ｘ",
                "Ｙ", "Ｚ", "－", "＿", "＝", "＋", "＼", "｜", "【", "】", "；", "：",
                "'", "\"", "，", "〈", "。", "〉", "／", "？"};
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
     * 将字符转换为编码为Unicode，格式 为'\u0020'<br>
     * unicodeEscaped(' ') = "\u0020"<br>
     * unicodeEscaped('A') = "\u0041"
     *
     * @param ch 待转换的char 字符
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
     * @param object  要进行toString操作的对象
     * @param nullStr 返回的默认值
     * @return
     */
    public static String toString(Object object, String nullStr) {
        return object == null ? nullStr : object.toString();
    }


    /**
     * 页面中去除字符串中的空格、回车、换行符、制表符
     */
    public static String replaceBlank(String str) {
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
        }
        return str;
    }

    /**
     * 字符串转换unicode.实现native2ascii.exe类似的功能
     *
     * @param string 需要处理的字符串
     */
    public static String string2Unicode(String string) {
        StringBuilder uni = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            String temp = "\\u" + Integer.toHexString(string.charAt(i));
            uni.append(temp);
        }
        return uni.toString();
    }

    /**
     * 转字符串 实现native2ascii.exe类似的功能
     *
     * @param unicode 需要处理的字符串
     */
    public static String unicode2String(String unicode) {
        StringBuilder str = new StringBuilder();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            int data = Integer.parseInt(hex[i], 16);
            str.append((char) data);
        }
        return str.toString();
    }


    /**
     * 返回俩个字符串共同的前缀
     */
    public static String hasPrefix(String str1, String str2) {
        //@TODO-字符串方法实现
        return null;
    }

    /**
     * 返回俩个字符串共同的后缀
     */
    public static String hasSuffix(String str1, String str2) {
        //TODO-字符串方法实现
        return null;
    }

    /**
     * 数字格式化
     */
    public static String formatNumber(BigDecimal obj, String format) {
        DecimalFormat df = new DecimalFormat(format);
        if (obj.compareTo(BigDecimal.ZERO) > 0 && obj.compareTo(new BigDecimal(1)) < 0) {
            return "0" + df.format(obj);
        } else {
            return df.format(obj);
        }
    }

    /**
     * 判断字符串是否有长度
     */
    public static boolean hasLength(String str) {
        return str != null && !str.isEmpty();
    }

    /**
     * 下划线转驼峰命名
     */
    public static String toUnderScoreCase(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            boolean nextUpperCase = true;

            if (i < (str.length() - 1)) {
                nextUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }


    /**
     * 判断字符串不为空
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !"".equals(str);
    }

    /**
     * 格式化一个float
     *
     * @param format 要格式化成的格式 such as #.00, #.#
     * @return 格式化后的字符串
     */
    public static String formatDouble(double f, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(f);
    }

    public static boolean isFormat(String message) {
        Matcher matcher = Patterns.FORMAT_PATTERN.matcher(message);
        return matcher.find();
    }
}
