package com.zifang.util.core.lang.converter.builtin;

import com.zifang.util.core.lang.converter.ConvertException;
import com.zifang.util.core.lang.converter.ConvertPolicy;
import com.zifang.util.core.lang.converter.ConverterScope;
import com.zifang.util.core.lang.converter.EnumValue;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 枚举族：String→enum。enum 常量类型是业务方定义的，无法预注册具体类型对，
 * 因此挂在 String→Object 上作为兜底候选——有更精确的注册项时它们排在前面。
 * <p>
 * 字面量匹配顺序：{@link ConvertPolicy#getEnumAliases()} 别名 →
 * {@link EnumValue} 标注字段的值 → 常量名。三段都不命中才判失败。
 *
 * @author zifang
 */
public final class EnumFamily {

    private static final Map<Class<?>, Field[]> VALUE_FIELDS = new ConcurrentHashMap<>();

    private EnumFamily() {
    }

    public static void register(ConverterScope scope) {
        BuiltInConverters.register(scope, String.class, Object.class, (value, context) -> {
            Class<?> target = context.getTargetType();
            if (target == null || !target.isEnum()) {
                throw new ConvertException(value, String.class, target, "兜底转换器只处理枚举目标类型");
            }
            return parse(value, Inputs.trim(value, context.getPolicy()), target, context.getPolicy());
        }, Integer.MIN_VALUE);
    }

    private static Object parse(Object original, String text, Class<?> enumType, ConvertPolicy policy) {
        String aliased = policy.getEnumAliases().get(text);
        if (aliased != null) {
            text = aliased;
        }
        Object[] constants = enumType.getEnumConstants();

        Field valueField = valueFieldOf(enumType);
        if (valueField != null) {
            for (Object constant : constants) {
                if (matches(text, readValue(valueField, constant), policy)) {
                    return constant;
                }
            }
            throw new ConvertException(original, String.class, enumType,
                    "枚举 " + enumType.getSimpleName() + " 无 " + valueField.getName() + " 为 " + text
                            + " 的常量，可选值=" + describeValues(valueField, constants));
        }

        for (Object constant : constants) {
            if (matches(text, ((Enum<?>) constant).name(), policy)) {
                return constant;
            }
        }
        throw new ConvertException(original, String.class, enumType,
                "枚举 " + enumType.getSimpleName() + " 无此常量，可选值=" + describe(constants));
    }

    private static boolean matches(String text, String candidate, ConvertPolicy policy) {
        if (candidate == null) {
            return false;
        }
        if (candidate.equals(text)) {
            return true;
        }
        return policy.getEnumMatchPolicy() == ConvertPolicy.EnumMatchPolicy.CASE_INSENSITIVE
                && candidate.equalsIgnoreCase(text);
    }

    private static Field valueFieldOf(Class<?> enumType) {
        Field[] found = VALUE_FIELDS.computeIfAbsent(enumType, type -> {
            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(EnumValue.class)) {
                    field.setAccessible(true);
                    return new Field[]{field};
                }
            }
            return new Field[0];
        });
        return found.length == 0 ? null : found[0];
    }

    private static String readValue(Field field, Object constant) {
        try {
            Object value = field.get(constant);
            return value == null ? null : value.toString();
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("读取 @EnumValue 字段失败: " + field, e);
        }
    }

    private static String describeValues(Field field, Object[] constants) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < constants.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(readValue(field, constants[i]));
        }
        return sb.append(']').toString();
    }

    private static String describe(Object[] constants) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < constants.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(((Enum<?>) constants[i]).name());
        }
        return sb.append(']').toString();
    }
}