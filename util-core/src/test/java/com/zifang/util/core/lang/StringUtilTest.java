package com.zifang.util.core.lang;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringUtilTest {
    @Test
    public void isBlank() {
        assertTrue(StringUtil.isBlank(null));
        assertTrue(StringUtil.isBlank(""));
        assertTrue(StringUtil.isBlank("   "));
        assertFalse(StringUtil.isBlank("Hello"));
        assertFalse(StringUtil.isBlank(" Hello "));
    }

    @Test
    public void isAllLowerCase() {

    }

    @Test
    public void isAllUpperCase() {
    }

    @Test
    public void isEmpty() {
    }

    @Test
    public void isNotEmptyString() {
    }

    @Test
    public void append() {
    }

    @Test
    public void collapseWhitespace() {
    }

    @Test
    public void contains() {
    }

    @Test
    public void testContains() {
    }

    @Test
    public void containsAll() {
    }

    @Test
    public void testContainsAll() {
    }

    @Test
    public void containsAny() {
    }

    @Test
    public void testContainsAny() {
    }

    @Test
    public void countSubstr() {
    }

    @Test
    public void testCountSubstr() {
    }

    @Test
    public void endsWith() {
    }

    @Test
    public void testEndsWith() {
    }

    @Test
    public void testEndsWith1() {
    }

    @Test
    public void ensureLeft() {
    }

    @Test
    public void testEnsureLeft() {
    }

    @Test
    public void ensureRight() {
    }

    @Test
    public void testEnsureRight() {
    }

    @Test
    public void base64Decode() {
    }

    @Test
    public void base64Encode() {
    }

    @Test
    public void binDecode() {
    }

    @Test
    public void binEncode() {
    }

    @Test
    public void decDecode() {
    }

    @Test
    public void decEncode() {
    }

    @Test
    public void decode() {
    }

    @Test
    public void encode() {
    }

    @Test
    public void first() {
    }

    @Test
    public void head() {
    }

    @Test
    public void format() {
    }

    @Test
    public void hexDecode() {
    }

    @Test
    public void hexEncode() {
    }

    @Test
    public void indexOf() {
    }

    @Test
    public void unequal() {
    }

    @Test
    public void inequal() {
    }

    @Test
    public void insert() {
    }

    @Test
    public void isUpperCase() {
    }

    @Test
    public void slugify() {
    }

    @Test
    public void transliterate() {
    }

    @Test
    public void surround() {
    }

    @Test
    public void toCamelCase() {
    }

    @Test
    public void toStudlyCase() {
    }

    @Test
    public void tail() {
    }

    @Test
    public void toDecamelize() {
    }

    @Test
    public void toKebabCase() {
    }

    @Test
    public void toSnakeCase() {
    }

    @Test
    public void capitalize() {
    }

    @Test
    public void lowerFirst() {
    }

    @Test
    public void isEnclosedBetween() {
    }

    @Test
    public void testIsEnclosedBetween() {
    }

    @Test
    public void upperFirst() {
    }

    @Test
    public void trimStart() {
    }

    @Test
    public void charsCount() {
    }

    @Test
    public void underscored() {
    }

    @Test
    public void lines() {
    }

    @Test
    public void dasherize() {
    }

    @Test
    public void humanize() {
    }

    @Test
    public void swapCase() {
    }

    @Test
    public void formatNumber() {
    }

    @Test
    public void chop() {
    }

    @Test
    public void startCase() {
    }

    @Test
    public void escapeRegExp() {
    }

    @Test
    public void rightPad() {
        assert StringUtil.rightPad("2","0",2).equals("20");
        assert StringUtil.rightPad("12","0",2).equals("12");
        assert StringUtil.rightPad("012","0",2).equals("012");
    }

    @Test
    public void leftPad() {
        assert StringUtil.leftPad("2","0",2).equals("02");
        assert StringUtil.leftPad("12","0",2).equals("12");
        assert StringUtil.leftPad("012","0",2).equals("012");
    }

    @Test
    public void repeat() {
        assert StringUtil.repeat("2",2).equals("22");
        assert StringUtil.repeat("-2",2).equals("-2-2");
    }

    @Test
    public void changeToFull() {

    }

    @Test
    public void unicodeEscaped() {
    }

    @Test
    public void testToString() {
    }

    @Test
    public void replaceBlank() {
    }

    @Test
    public void string2Unicode() {
    }

    @Test
    public void unicode2String() {
    }

    @Test
    public void hasPrefix() {
    }

    @Test
    public void hasSuffix() {
    }

    @Test
    public void testFormatNumber() {
    }

    @Test
    public void hasLength() {
    }

    @Test
    public void toUnderScoreCase() {
    }

    @Test
    public void isNotEmpty() {
    }

    @Test
    public void formatDouble() {
    }

    @Test
    public void isFormat() {
    }

    @Test
    public void testBase64Decode() {
        assert StringUtil.base64Decode("aGVsbG8=").equals("hello");
    }

    @Test
    public void testBase64Encode() {
        assert StringUtil.base64Encode("hello").equals("aGVsbG8=");
    }

    @Test
    public void testBinDecode() {

        assert StringUtil.binEncode("\0\1").equals("00000000000000000000000000000001");
        assert StringUtil.binDecode("00000000000000000000000000000001").equals("\0\1");
        assert StringUtil.decEncode("\0\1").equals("0000000001");
        assert StringUtil.decDecode("0000000001").equals("\0\1");
//      assert StringUtil.hexDecode("0110100001100101011011000110110001101111").equals("hello");

    }
}