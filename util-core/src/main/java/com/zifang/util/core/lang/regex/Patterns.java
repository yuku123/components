package com.zifang.util.core.lang.regex;

import java.util.regex.Pattern;


public class Patterns {

    public static String FORMAT_SPECIFIER = "%(\\d+\\$)?([-#+ 0,(<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])";
    public static String FLOATING_POINT_NUMBER_FORMAT = "^[-\\+]?[.\\d]*$";

    public static Pattern FORMAT_PATTERN = Pattern.compile(FORMAT_SPECIFIER);
    public static Pattern FLOATING_POINT_NUMBER_PATTERN = Pattern.compile(FLOATING_POINT_NUMBER_FORMAT);
}
