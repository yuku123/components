package com.zifang.util.core.common.constant;

import java.util.regex.Pattern;

import static com.zifang.util.core.common.constant.StringConstant.FLOATING_POINT_NUMBER_FORMAT;
import static com.zifang.util.core.common.constant.StringConstant.FORMAT_SPECIFIER;

/**
 * @author: Ares
 * @time: 2021-12-21 17:36:59
 * @description: common constant
 * @version: JDK 1.8
 */
public interface CommonConstant {

  Pattern FORMAT_PATTERN = Pattern.compile(FORMAT_SPECIFIER);

  Pattern FLOATING_POINT_NUMBER_PATTERN = Pattern.compile(FLOATING_POINT_NUMBER_FORMAT);

  /**
   * false with byte type
   */
  byte BYTE_FALSE = (byte) 0;
  /**
   * true with byte type
   */
  byte BYTE_TRUE = (byte) 1;

  byte FALSE = 0;
  byte TRUE = 1;

  /**
   * Unicode 基本汉字编码范围0x4e00~0x9fa5 共 20902个
   */
  int CHINESE_CHARACTER_LENGTH = 20902;

  /**
   * 汉字起始值
   */
  int CHINESE_CHARACTER_START = 0x4e00;

}
