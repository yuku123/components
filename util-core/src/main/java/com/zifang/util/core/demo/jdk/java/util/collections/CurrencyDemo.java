package com.zifang.util.core.demo.jdk.java.util.collections;

import java.util.Currency;
import java.util.Locale;

/**
 * java.spi.Currency 类代表货币
 *
 *
 */
public class CurrencyDemo {

	public static void test1(Currency currency) {
		//获取此货币的符号为默认语言环境。
		System.out.println("United States:" + currency.getSymbol());
		//获取此货币的ISO4217货币代码
		System.out.println("CurrencyCode:" + currency.getCurrencyCode());
		System.out.println("DisplayName:" + currency.getDisplayName());
		System.out.println("NumericCode:" + currency.getNumericCode());
		System.out.println("AvailableCurrencies:" + currency.getAvailableCurrencies());

		System.out.println("----------");
	}

	public static void main(String[] args) {
		Currency currency = Currency.getInstance(Locale.US);
		test1(currency);
		currency = Currency.getInstance(Locale.CHINA);
		test1(currency);

	}
}
