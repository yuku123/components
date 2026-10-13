package com.zifang.util.sensitive.pii;

import com.zifang.util.sensitive.SensitiveType;

/**
 * 银行卡号。按发卡行 BIN 前缀召回，再用 Luhn 校验位定精确率。
 */
public class BankCardRule extends PiiRule {

    /** Visa / MasterCard / American Express / Discover / 银联 */
    private static final String REGEX =
            "(?<![0-9])("
                    + "4\\d{12}(?:\\d{3})?"
                    + "|5[1-5]\\d{14}"
                    + "|3[47]\\d{13}"
                    + "|6(?:011|5\\d{2})\\d{12}"
                    + "|62\\d{14,17}"
                    + ")(?![0-9])";

    public BankCardRule() {
        this("bank_card");
    }

    public BankCardRule(String tag) {
        super(REGEX, tag);
    }

    @Override
    public SensitiveType type() {
        return SensitiveType.BANK_CARD;
    }

    @Override
    protected boolean accept(String candidate) {
        return luhn(candidate);
    }

    static boolean luhn(String number) {
        int sum = 0;
        boolean doubleDigit = false;
        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = number.charAt(i) - '0';
            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            doubleDigit = !doubleDigit;
        }
        return sum % 10 == 0;
    }
}
