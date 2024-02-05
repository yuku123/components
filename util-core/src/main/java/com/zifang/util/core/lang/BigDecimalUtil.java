package com.zifang.util.core.lang;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 提供精确的加减乘除运算
 */
public class BigDecimalUtil {

    /**
     * 默认保留位：2
     */
    private static int DEFAULT_SCALE = 2;

    /**
     * 默认四舍五入规则为：向上舍入
     */
    private static int DEFAULT_ROUND = BigDecimal.ROUND_HALF_UP;

    /**
     * 加法运算
     *
     * @param v1 加数
     * @param v2 被加数
     * @return
     */
    public static String add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).toString();
    }

    /**
     * 除法运算<br>
     * 当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
     *
     * @param v1    除数
     * @param v2    被除数
     * @param scale 精确精度
     * @return
     */
    public static String div(String v1, String v2, int scale, int round) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }

//		if (ValidateHelper.isEmpty(scale)) {
//			scale = DEFAULT_SCALE;
//		}
//
//		if (ValidateHelper.isEmpty(round)) {
//			round = DEFAULT_ROUND;
//		}

        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 比较两个数<br>
     * v1 > v2 return 1<br>
     * v1 = v2 return 0<br>
     * v1 < v2 return -1
     *
     * @param v1 比较数
     * @param v2 被比较数
     * @return
     */
    public static int compareTo(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.compareTo(b2);
    }

    /**
     * 返回较小数
     *
     * @param v1
     * @param v2
     * @return
     */
    public static String returnMin(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.min(b2).toString();
    }

    /**
     * 返回较大数
     *
     * @param v1
     * @param v2
     * @return
     */
    public static String returnMax(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.max(b2).toString();
    }

    /**
     * 处理BigDecimal数据，保留scale位小数
     *
     * @param value
     * @param scale
     * @return
     */
    public static BigDecimal getValue(BigDecimal value, int scale) {
//		if(!ValidateHelper.isEmpty(value)){
//			return value.setScale(scale, BigDecimal.ROUND_HALF_UP);
//		}
        return value;
    }

    /**
     * 将object转换为Bigdecimal
     *
     * @param value 待转换的数值
     * @return
     */
    public static BigDecimal getBigDecimal(Object value) {
        BigDecimal resultValue = new BigDecimal(0);
        if (value instanceof String) {
            resultValue = new BigDecimal((String) value);
        } else if (value instanceof Integer) {
            resultValue = new BigDecimal((Integer) value);
        } else if (value instanceof Long) {
            resultValue = new BigDecimal((Long) value);
        } else if (value instanceof Double) {
            resultValue = new BigDecimal((Double) value);
        } else {
            resultValue = (BigDecimal) value;
        }

        return resultValue;
    }


    /**
     * 将object转换为Bigdecimal,若object为空，则返回resultValue
     *
     * @param value
     * @return
     */
    public static BigDecimal getBigDecimal(Object value, BigDecimal resultValue) {
//		if(ValidateHelper.isEmpty(value)){
//			return resultValue;
//		}

        resultValue = getBigDecimal(resultValue);

        return resultValue;
    }

    /**
     * 将BigDecimal 转换成Long
     *
     * @param value
     * @return
     */
    public static Long bigDecimalToLong(BigDecimal value) {
        if (value != null) {
            return new Long(value.longValue());
        }
        return null;
    }

    /**
     * 将BigDecimal 转换成integer
     *
     * @param value
     * @return
     */
    public static Integer bigDecimalToInteger(BigDecimal value) {
        if (value != null) {
            return new Integer(value.intValue());
        }
        return null;
    }

    /**
     * 默认除法运算精度
     */
    private static final int DEF_DIV_SCALE = 10;

    /**
     * 提供精确的加法运算
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
        if (Objects.isNull(v1)) {
            v1 = BigDecimal.ZERO;
        }
        if (Objects.isNull(v2)) {
            v2 = BigDecimal.ZERO;
        }
        return v1.add(v2);
    }

    /**
     * 提供精确的加法运算
     *
     * @param v1    被加数
     * @param v2    加数
     * @param scale 保留scale 位小数(必须>=0)
     * @return 两个参数的和
     */
    public static BigDecimal add(BigDecimal v1, BigDecimal v2, int scale) {
        if (scale < 0) {
            return BigDecimal.ZERO;
        }
        if (Objects.isNull(v1)) {
            v1 = BigDecimal.ZERO;
        }
        if (Objects.isNull(v2)) {
            v2 = BigDecimal.ZERO;
        }
        return v1.add(v2).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供精确的减法运算
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
        if (Objects.isNull(v1)) {
            v1 = BigDecimal.ZERO;
        }
        if (Objects.isNull(v2)) {
            v2 = BigDecimal.ZERO;
        }
        return v1.subtract(v2);
    }


    /**
     * 提供精确的减法运算
     *
     * @param v1    被减数
     * @param v2    减数
     * @param scale 保留scale 位小数(必须>=0)
     * @return 两个参数的差
     */
    public static BigDecimal sub(BigDecimal v1, BigDecimal v2, int scale) {
        if (scale < 0) {
            return new BigDecimal(0);
        }
        if (Objects.isNull(v1)) {
            v1 = BigDecimal.ZERO;
        }
        if (Objects.isNull(v2)) {
            v2 = BigDecimal.ZERO;
        }
        return v1.subtract(v2).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供精确的乘法运算
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
        if (Objects.isNull(v1)) {
            v1 = BigDecimal.ZERO;
        }
        if (Objects.isNull(v2)) {
            v2 = BigDecimal.ZERO;
        }
        return v1.multiply(v2);
    }


    /**
     * 提供精确的乘法运算
     *
     * @param v1    被乘数
     * @param v2    乘数
     * @param scale 保留scale 位小数(必须>=0)
     * @return 两个参数的积
     */
    public static BigDecimal mul(BigDecimal v1, BigDecimal v2, int scale) {
        if (scale < 0) {
            return new BigDecimal(0);
        }
        if (Objects.isNull(v1)) {
            v1 = BigDecimal.ZERO;
        }
        if (Objects.isNull(v2)) {
            v2 = BigDecimal.ZERO;
        }
        return v1.multiply(v2).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，默认计算到10位，以后的数字四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要保留的小数位数。
     * @return 两个参数的商
     */
    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale) {
        if (scale < 0) {
            return new BigDecimal(0);
        }
        return round(v1.divide(v2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP), scale);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，计算到accuracy位，以后的数字四舍五入
     *
     * @param v1       被除数
     * @param v2       除数
     * @param scale    表示表示需要保留的小数位数。(必须>=0)
     * @param accuracy 计算精度
     * @return 两个参数的商
     */
    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale, int accuracy) {
        if (scale < 0) {
            return new BigDecimal(0);
        }
        return round(v1.divide(v2, accuracy, BigDecimal.ROUND_HALF_UP), scale);
    }


    /**
     * 提供精确的小数位四舍五入处理
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static BigDecimal round(BigDecimal v, int scale) {
        if (scale < 0) {
            return v;
        }
        return v.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供向上取整
     *
     * @param v     需要线上取整的数字
     * @param scale 小数点后保留几位
     * @return 线上取整的结果
     */
    public static BigDecimal roundUp(BigDecimal v, int scale) {
        if (scale < 0) {
            return v;
        }
        return v.setScale(scale, BigDecimal.ROUND_UP);
    }


    /**
     * 取余数  BigDecimal
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 小数点后保留几位
     * @return 余数
     */
    public static BigDecimal remainder(BigDecimal v1, BigDecimal v2, int scale) {
        if (scale < 0) {
            return new BigDecimal(0);
        }
        return v1.remainder(v2).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 比较大小
     *
     * @param v1 被比较数
     * @param v2 比较数
     * @return 如果v1 大于v2 则 返回true 否则false
     */
    public static boolean compare(BigDecimal v1, BigDecimal v2) {
        if (Objects.isNull(v1)) {
            v1 = BigDecimal.ZERO;
        }
        if (Objects.isNull(v2)) {
            v2 = BigDecimal.ZERO;
        }
        int bj = v1.compareTo(v2);
        return bj > 0;
    }

    /**
     * 比较大小
     *
     * @param v1 被比较数
     * @param v2 比较数
     * @return 如果v1 大于或等于v2 则 返回true 否则false
     */
    public static boolean compareGreater(BigDecimal v1, BigDecimal v2) {
        if (Objects.isNull(v1)) {
            v1 = BigDecimal.ZERO;
        }
        if (Objects.isNull(v2)) {
            v2 = BigDecimal.ZERO;
        }
        int bj = v1.compareTo(v2);
        return bj >= 0;
    }

    /**
     * 是否相等
     *
     * @param v1 被比较数
     * @param v2 比较数
     * @return 如果v1 等 v2 则 返回true 否则false
     */
    public static boolean compareEqual(BigDecimal v1, BigDecimal v2) {
        if (Objects.isNull(v1)) {
            v1 = BigDecimal.ZERO;
        }
        if (Objects.isNull(v2)) {
            v2 = BigDecimal.ZERO;
        }
        int bj = v1.compareTo(v2);
        return bj == 0;
    }

    /**
     * 是否相等
     *
     * @param v1 被比较数
     * @param v2 比较数
     * @return 如果v1 等 v2 则 返回true 否则false
     */
    public static boolean compareEqual(Long v1, Long v2) {
        if (Objects.isNull(v1)) {
            v1 = 0L;
        }
        if (Objects.isNull(v2)) {
            v2 = 0L;
        }
        int bj = v1.compareTo(v2);
        return bj == 0;
    }

    /**
     * 两数相除，返回商与余
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 数组第一位为商，第二位为余
     */
    public static BigDecimal[] divideAndRemainder(BigDecimal v1, BigDecimal v2) {
        return v1.divideAndRemainder(v2);
    }

    /**
     * BigDecimal 2 String
     *
     * @param data  数据
     * @param scale 精度
     * @return
     */
    public static String bigDecimal2String(BigDecimal data, int scale) {
        if (data != null) {
            return data.setScale(scale, BigDecimal.ROUND_DOWN).toPlainString();
        }
        return null;
    }
}
