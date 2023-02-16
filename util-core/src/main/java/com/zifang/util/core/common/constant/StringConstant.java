package com.zifang.util.core.common.constant;

/**
 * @author: Ares
 * @time: 2021-12-26 17:27:08
 * @description: string constant
 * @version: JDK 1.8
 */
public interface StringConstant {

  String ZERO = "0";
  String ONE = "1";
  String TRUE = "true";
  String FALSE = "false";

  String EMPTY = "";
  String[] EMPTY_ARRAY = new String[0];

  String DEFAULT = "default";

  String FORMAT_SPECIFIER = "%(\\d+\\$)?([-#+ 0,(<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])";

  String FLOATING_POINT_NUMBER_FORMAT = "^[-\\+]?[.\\d]*$";
  String LINE_BREAK = "\n";

}
