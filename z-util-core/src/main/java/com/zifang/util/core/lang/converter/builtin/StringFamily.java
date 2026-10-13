package com.zifang.util.core.lang.converter.builtin;

import com.zifang.util.core.lang.converter.ConvertException;
import com.zifang.util.core.lang.converter.Converter;
import com.zifang.util.core.lang.converter.ConverterScope;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 字符串族：任意对象→String、String→char、String→boolean。
 *
 * @author zifang
 */
public final class StringFamily {

    private StringFamily() {
    }

    public static void register(ConverterScope scope) {
        BuiltInConverters.register(scope, Object.class, String.class, (value, context) -> {
            String text = render(value);
            return context.getPolicy().isTrim() ? text.trim() : text;
        });

        BuiltInConverters.register(scope, String.class, Character.class, (value, context) -> {
            String text = Inputs.trim(value, context.getPolicy());
            if (text.length() != 1) {
                throw new ConvertException(value, String.class, Character.class,
                        "长度为 " + text.length() + "，无法转为 char");
            }
            return text.charAt(0);
        });

        BuiltInConverters.register(scope, String.class, Boolean.class, (value, context) -> {
            String text = Inputs.trim(value, context.getPolicy());
            String lower = text.toLowerCase();
            if (context.getPolicy().getTrueValues().contains(lower)) {
                return Boolean.TRUE;
            }
            if (context.getPolicy().getFalseValues().contains(lower)) {
                return Boolean.FALSE;
            }
            throw new ConvertException(value, String.class, Boolean.class,
                    "不在布尔词表内，真值=" + context.getPolicy().getTrueValues()
                            + " 假值=" + context.getPolicy().getFalseValues());
        });
    }

    /**
     * 数组的字符串化：对象数组走 deepToString，基本类型数组逐元素拼接，避免打印成 [I@1a2b3c
     */
    static String render(Object value) {
        Class<?> type = value.getClass();
        if (!type.isArray()) {
            return String.valueOf(value);
        }
        if (Object.class.isAssignableFrom(type.getComponentType())) {
            return Arrays.deepToString((Object[]) value);
        }
        StringBuilder sb = new StringBuilder("[");
        int length = Array.getLength(value);
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(Array.get(value, i));
        }
        return sb.append(']').toString();
    }
}