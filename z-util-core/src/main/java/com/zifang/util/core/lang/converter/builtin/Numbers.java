package com.zifang.util.core.lang.converter.builtin;

import com.zifang.util.core.lang.converter.ConvertException;
import com.zifang.util.core.lang.converter.ConvertPolicy;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 数值窄化与精度处理。
 * <p>
 * 统一走 BigDecimal 中间表示，避免 double→long 这类路径上的精度意外丢失，
 * 越界处置由 {@link ConvertPolicy.NarrowingPolicy} 决定。
 *
 * @author zifang
 */
final class Numbers {

    private Numbers() {
    }

    /**
     * 把任意 Number 收敛成 BigDecimal。double/float 走字符串路径，保住十进制字面量原值。
     */
    static BigDecimal toBigDecimal(Number value, ConvertPolicy policy) {
        BigDecimal decimal;
        if (value instanceof BigDecimal) {
            decimal = (BigDecimal) value;
        } else if (value instanceof BigInteger) {
            decimal = new BigDecimal((BigInteger) value);
        } else if (value instanceof Double || value instanceof Float) {
            decimal = new BigDecimal(value.toString());
        } else {
            decimal = BigDecimal.valueOf(value.longValue());
        }
        int scale = policy.getScale();
        return scale >= 0 ? decimal.setScale(scale, policy.getRoundingMode()) : decimal;
    }

    /**
     * 按目标类型收窄。target 必须是数值类型。
     */
    static Object narrow(Number value, Class<?> target, ConvertPolicy policy) {
        if (target == BigDecimal.class) {
            return toBigDecimal(value, policy);
        }
        if (target == BigInteger.class) {
            return toBigDecimal(value, policy).toBigInteger();
        }
        if (target == Float.class) {
            return checkReal(value, policy).floatValue();
        }
        if (target == Double.class) {
            return checkReal(value, policy).doubleValue();
        }
        if (target == Byte.class) {
            return integral(value, target, Byte.MIN_VALUE, Byte.MAX_VALUE, policy).byteValue();
        }
        if (target == Short.class) {
            return integral(value, target, Short.MIN_VALUE, Short.MAX_VALUE, policy).shortValue();
        }
        if (target == Integer.class) {
            return integral(value, target, Integer.MIN_VALUE, Integer.MAX_VALUE, policy).intValue();
        }
        if (target == Long.class) {
            return integral(value, target, Long.MIN_VALUE, Long.MAX_VALUE, policy).longValue();
        }
        throw new ConvertException(value, value.getClass(), target, "不是可收窄的数值目标类型");
    }

    private static BigDecimal checkReal(Number value, ConvertPolicy policy) {
        double d = toBigDecimal(value, policy).doubleValue();
        if (!Double.isInfinite(d) && !Double.isNaN(d)) {
            return BigDecimal.valueOf(d);
        }
        switch (policy.getNarrowingPolicy()) {
            case SATURATE:
                return BigDecimal.valueOf(d > 0 ? Double.MAX_VALUE : -Double.MAX_VALUE);
            case WRAP:
                return BigDecimal.valueOf(Double.MAX_VALUE);
            case FAIL:
            default:
                throw new ConvertException(value, value.getClass(), Double.class,
                        "数值 " + value + " 超出浮点可表示范围");
        }
    }

    private static BigInteger integral(Number value, Class<?> target, long min, long max, ConvertPolicy policy) {
        BigDecimal rounded = toBigDecimal(value, policy).setScale(0, policy.getRoundingMode());
        BigInteger integer = rounded.toBigInteger();
        if (integer.compareTo(BigInteger.valueOf(min)) >= 0 && integer.compareTo(BigInteger.valueOf(max)) <= 0) {
            return integer;
        }
        switch (policy.getNarrowingPolicy()) {
            case SATURATE:
                return integer.compareTo(BigInteger.valueOf(min)) < 0 ? BigInteger.valueOf(min) : BigInteger.valueOf(max);
            case WRAP:
                return BigInteger.valueOf(integer.longValue());
            case FAIL:
            default:
                throw new ConvertException(value, value.getClass(), target,
                        "数值 " + value + " 超出 " + target.getSimpleName() + " 的 [" + min + ", " + max + "] 范围");
        }
    }

    }