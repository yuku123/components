package com.zifang.util.core;

import java.util.regex.Pattern;

import static com.zifang.util.core.common.constant.StringConstant.FLOATING_POINT_NUMBER_FORMAT;
import static com.zifang.util.core.common.constant.StringConstant.FORMAT_SPECIFIER;

public class Patterns {
    public static Pattern FORMAT_PATTERN = Pattern.compile(FORMAT_SPECIFIER);
    public static  Pattern FLOATING_POINT_NUMBER_PATTERN = Pattern.compile(FLOATING_POINT_NUMBER_FORMAT);
}
