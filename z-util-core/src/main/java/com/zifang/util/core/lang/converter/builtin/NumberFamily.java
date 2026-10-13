package com.zifang.util.core.lang.converter.builtin;

import com.zifang.util.core.lang.converter.ConvertException;
import com.zifang.util.core.lang.converter.ConverterScope;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;

/**
 * 数值族：String→8 种数值类型、Number→8 种数值类型（收窄走 {@link Numbers}）。
 *
 * @author zifang
 */
public final class NumberFamily {

    private NumberFamily() {
    }

    public static void register(ConverterScope scope) {
        registerTarget(scope, Byte.class, Byte::valueOf);
        registerTarget(scope, Short.class, Short::valueOf);
        registerTarget(scope, Integer.class, Integer::valueOf);
        registerTarget(scope, Long.class, Long::valueOf);
        registerTarget(scope, Float.class, Float::valueOf);
        registerTarget(scope, Double.class, Double::valueOf);
        registerTarget(scope, BigInteger.class, BigInteger::new);
        registerTarget(scope, BigDecimal.class, BigDecimal::new);
    }

    private static <T> void registerTarget(ConverterScope scope, Class<T> target, Function<String, T> parser) {
        BuiltInConverters.register(scope, String.class, target, (value, context) -> {
            String text = Inputs.trim(value, context.getPolicy());
            T parsed;
            try {
                parsed = parser.apply(text);
            } catch (NumberFormatException e) {
                throw new ConvertException(value, String.class, target,
                        "无法解析为 " + target.getSimpleName() + "：" + e.getMessage());
            }
            if (parsed instanceof BigDecimal) {
                // scale 策略对字符串直解同样生效，否则 String→BigDecimal 会绕过它
                return (T) Numbers.toBigDecimal((BigDecimal) parsed, context.getPolicy());
            }
            return parsed;
        });

        BuiltInConverters.register(scope, Number.class, target,
                (value, context) -> (T) Numbers.narrow(value, target, context.getPolicy()));
    }
}