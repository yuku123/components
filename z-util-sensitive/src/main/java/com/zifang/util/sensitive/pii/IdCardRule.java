package com.zifang.util.sensitive.pii;

import com.zifang.util.sensitive.SensitiveType;

/**
 * 身份证号。按 GB11643-1999：18 位、六位地址码 + 八位出生日期码 + 三位顺序码 + 一位校验码。
 * <p>
 * 只收 18 位。15 位是 2000 年前的老号，正常文本里几乎不出现；为了它再写一套转换逻辑不值当。
 */
public class IdCardRule extends PiiRule {

    private static final int[] WEIGHTS = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static final char[] CHECK_CODES = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    /** 省级行政区划代码前两位 */
    private static final String PROVINCES =
            "11,12,13,14,15,21,22,23,31,32,33,34,35,36,37,41,42,43,44,45,46,50,51,52,53,54,"
                    + "61,62,63,64,65,71,81,82,91";

    private static final String REGEX =
            "(?<![0-9A-Za-z])[1-9]\\d{5}(19|20)\\d{2}"
                    + "(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])"
                    + "\\d{3}[\\dXx](?![0-9A-Za-z])";

    public IdCardRule() {
        this("id_card");
    }

    public IdCardRule(String tag) {
        super(REGEX, tag);
    }

    @Override
    public SensitiveType type() {
        return SensitiveType.ID_CARD;
    }

    @Override
    protected boolean accept(String candidate) {
        if (candidate.length() != 18) {
            return false;
        }
        if (PROVINCES.indexOf(candidate.substring(0, 2)) < 0) {
            return false;
        }
        if (!validBirthday(candidate.substring(6, 14))) {
            return false;
        }
        return checkCode(candidate) == Character.toUpperCase(candidate.charAt(17));
    }

    private static boolean validBirthday(String yyyyMMdd) {
        int year = Integer.parseInt(yyyyMMdd.substring(0, 4));
        int month = Integer.parseInt(yyyyMMdd.substring(4, 6));
        int day = Integer.parseInt(yyyyMMdd.substring(6, 8));
        if (year < 1900 || month < 1 || month > 12 || day < 1) {
            return false;
        }
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.clear();
        cal.setLenient(false);
        cal.set(year, month - 1, day);
        try {
            cal.getTime();
            return true;
        } catch (IllegalArgumentException e) {
            // 非闰年 2 月 29 日之类
            return false;
        }
    }

    private static char checkCode(String id) {
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += (id.charAt(i) - '0') * WEIGHTS[i];
        }
        return CHECK_CODES[sum % 11];
    }
}
